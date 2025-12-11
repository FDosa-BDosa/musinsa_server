package com.mudosa.musinsa.chat.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FanoutExecutor {

  private final SessionQueue.WebSocketSessionManager sessionManager;
  private final ObjectWriter writer = new ObjectMapper().writer();
  private final ShardedFanoutWorker shardedFanoutWorker;

  public void execute(List<String> sessionIds, Object payload) {
    final String json;

    try {
      json = writer.writeValueAsString(payload);
    } catch (Exception e) {
      return;
    }

    TextMessage msg = new TextMessage(json);

    for (String sid : sessionIds) {
      SessionQueue q = sessionManager.getQueue(sid);
      if (q != null) {
        q.enqueue(msg);
        shardedFanoutWorker.notifySessionForSend(sid);
      }
    }
  }
}
