package com.mudosa.musinsa.chat.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class SessionQueue {
  private final BlockingQueue<TextMessage> queue = new LinkedBlockingQueue<>();

  public boolean isEmpty() {
    return queue.isEmpty();
  }

  public void enqueue(TextMessage msg) {
    queue.offer(msg);
  }

  public TextMessage poll() {
    return queue.poll();
  }

  @Component
  public static class WebSocketSessionManager {

    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, SessionQueue> sessionQueues = new ConcurrentHashMap<>();

    public void addSession(WebSocketSession session) {
      sessions.put(session.getId(), session);
      sessionQueues.put(session.getId(), new SessionQueue());
    }

    public void removeSession(WebSocketSession session) {
      sessions.remove(session.getId());
      sessionQueues.remove(session.getId());
    }

    public SessionQueue getQueue(String sessionId) {
      return sessionQueues.get(sessionId);
    }

    public WebSocketSession getSession(String sessionId) {
      return sessions.get(sessionId);
    }
  }
}
