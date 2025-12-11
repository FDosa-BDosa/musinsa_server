package com.mudosa.musinsa.chat.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mudosa.musinsa.chat.service.RedisSubscriberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final RedisSubscriberService subscriberService;

  @Override
  public void afterConnectionEstablished(WebSocketSession session) {
    log.info("[WS] Connected sessionId={}", session.getId());
  }

  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

    JsonNode json = objectMapper.readTree(message.getPayload());
    String type = json.get("type").asText();

    switch (type) {

      case "CONNECT" -> {
        long userId = json.get("userId").asLong();
        session.getAttributes().put("userId", userId);
        subscriberService.registerSession(userId, session);

        log.info("[WS] CONNECT userId={} sessionId={}", userId, session.getId());
      }

      case "SUBSCRIBE" -> {
        long chatId = json.get("chatId").asLong();
        subscriberService.subscribeSession(session, chatId);

        log.info("[WS] SUBSCRIBE chatId={} sessionId={}", chatId, session.getId());
      }

      case "UNSUBSCRIBE" -> {
        long chatId = json.get("chatId").asLong();
        subscriberService.unsubscribeSession(session, chatId);

        log.info("[WS] UNSUBSCRIBE chatId={} sessionId={}", chatId, session.getId());
      }

      default -> log.warn("[WS] Unknown type: {}", type);
    }
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
    subscriberService.removeSession(session);
    log.info("[WS] Disconnected sessionId={}", session.getId());
  }
}
