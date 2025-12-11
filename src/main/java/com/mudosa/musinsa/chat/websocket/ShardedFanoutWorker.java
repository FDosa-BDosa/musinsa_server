package com.mudosa.musinsa.chat.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

@Slf4j
@Component
public class ShardedFanoutWorker {

  // 코어 대비 너무 크게 잡지 말고, 상한 두는 게 안전
  private static final int MAX_SHARDS = 16;
  private static final int SHARD_QUEUE_CAPACITY = 10_000; // shard당 세션 알림 대기 최대 개수

  private final int SHARD_COUNT;

  // shard별 세션 enqueue 큐
  private final List<BlockingQueue<String>> shardEventQueues;

  // shard별 "현재 큐에 있거나 처리 중인 세션ID" 집합 (중복 enqueue 방지 용도)
  private final List<Set<String>> shardPendingSessionSets;

  private final SessionQueue.WebSocketSessionManager sessionManager;
  private final ExecutorService workerPool;

  public ShardedFanoutWorker(SessionQueue.WebSocketSessionManager sessionManager) {
    this.sessionManager = sessionManager;

    int cores = Runtime.getRuntime().availableProcessors();
    this.SHARD_COUNT = Math.min(cores * 2, MAX_SHARDS); // 예: 4코어 → 8개, 8코어 → 16개

    this.shardEventQueues = new CopyOnWriteArrayList<>();
    this.shardPendingSessionSets = new CopyOnWriteArrayList<>();

    for (int i = 0; i < SHARD_COUNT; i++) {
      // capacity 있는 큐
      shardEventQueues.add(new LinkedBlockingQueue<>(SHARD_QUEUE_CAPACITY));
      // concurrent set (중복 enqueue 방지)
      shardPendingSessionSets.add(ConcurrentHashMap.newKeySet());
    }

    this.workerPool = Executors.newFixedThreadPool(SHARD_COUNT);

    for (int shard = 0; shard < SHARD_COUNT; shard++) {
      int idx = shard;
      workerPool.submit(() -> shardWorkerLoop(idx));
    }

    log.info("Initialized ShardedFanoutWorker - SHARD_COUNT={}, SHARD_QUEUE_CAPACITY={}",
        SHARD_COUNT, SHARD_QUEUE_CAPACITY);
  }

  private void shardWorkerLoop(int shardIndex) {
    BlockingQueue<String> sessionQueue = shardEventQueues.get(shardIndex);
    Set<String> pendingSet = shardPendingSessionSets.get(shardIndex);

    while (true) {
      try {
        // Blocking: 세션ID 하나 대기
        String sessionId = sessionQueue.take();

        WebSocketSession session = sessionManager.getSession(sessionId);
        SessionQueue msgQueue = sessionManager.getQueue(sessionId);

        if (session == null || msgQueue == null) {
          // 세션이 이미 끊겼거나 큐가 없으면 pendingSet 정리
          pendingSet.remove(sessionId);
          continue;
        }

        // 메시지가 있는 동안 모두 처리
        TextMessage msg;
        while ((msg = msgQueue.poll()) != null) {
          try {
            if (session.isOpen()) {
              session.sendMessage(msg);
            } else {
              break;
            }
          } catch (Exception e) {
            log.error("Shard {} - Send failed for session {}: {}", shardIndex, sessionId, e.getMessage(), e);
          }
        }

        // 이 시점에서 msgQueue는 비워진 상태
        //    → 이제 이 세션은 "더 이상 처리할 게 없다" → pendingSet에서 제거
        pendingSet.remove(sessionId);

      } catch (InterruptedException ie) {
        Thread.currentThread().interrupt();
        log.warn("Shard worker {} interrupted, exiting loop.", shardIndex);
        break;
      } catch (Exception e) {
        log.error("Shard worker {} error: {}", shardIndex, e.getMessage(), e);
      }
    }
  }

  public void notifySessionForSend(String sessionId) {
    // sessionId → shard 매핑
    int shard = Math.abs(sessionId.hashCode()) % SHARD_COUNT;
    BlockingQueue<String> queue = shardEventQueues.get(shard);
    Set<String> pendingSet = shardPendingSessionSets.get(shard);

    // 이미 이 shard에서 처리 대기/처리 중이면 큐에 다시 넣지 않음
    boolean firstRegister = pendingSet.add(sessionId);
    if (!firstRegister) {
      // 이미 큐에 있거나 처리 중 → noop
      return;
    }

    // 큐에 등록 시도 (capacity 넘으면 false)
    boolean offered = queue.offer(sessionId);
    if (!offered) {
      // 큐에 못 넣었으면 pendingSet 롤백
      pendingSet.remove(sessionId);
      log.warn("Shard {} queue is FULL (cap={}): drop notify for sessionId={}",
          shard, SHARD_QUEUE_CAPACITY, sessionId);
    }
  }
}
