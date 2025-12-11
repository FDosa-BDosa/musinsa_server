package com.mudosa.musinsa.chat.service;

import com.mudosa.musinsa.chat.websocket.SessionQueue;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisSubscriberService {

  private final StringRedisTemplate redis;
  private final SessionQueue.WebSocketSessionManager sessionManager;

  public void registerSession(long userId, WebSocketSession session) {
    // userId → sessionId 매핑
    redis.opsForValue().set("session:" + userId, session.getId());
    sessionManager.addSession(session);
  }

  public void subscribeSession(WebSocketSession session, long chatId) {
    redis.opsForSet().add("chatroom:" + chatId + ":subs", session.getId());
  }

  public void unsubscribeSession(WebSocketSession session, long chatId) {
    redis.opsForSet().remove("chatroom:" + chatId + ":subs", session.getId());
  }

  public void removeSession(WebSocketSession session) {
    // 세션 attribute에서 userId 꺼내기
    Object attr = session.getAttributes().get("userId");
    if (attr != null) {
      String userId = String.valueOf(attr);
      String key = "session:" + userId;

      // Redis 키 삭제
      redis.delete(key);
    }

    // 메모리에서도 제거
    sessionManager.removeSession(session);
  }

  public List<String> getSubscribers(Long chatId) {
    return new ArrayList<>(redis.opsForSet().members("chatroom:" + chatId + ":subs"));
  }

  public WebSocketSession getSession(String sessionId) {
    return sessionManager.getSession(sessionId);
  }
}
