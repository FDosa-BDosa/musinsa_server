package com.mudosa.musinsa.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mudosa.musinsa.chat.dto.wsDto.WsPushRequest;
import com.mudosa.musinsa.chat.service.RedisSubscriberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@RestController
@RequiredArgsConstructor
@RequestMapping("/_internal/ws")
public class WsPushController {

  private final RedisSubscriberService subscriberService;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @PostMapping("/push")
  public void push(@RequestBody WsPushRequest req) throws Exception {
    WebSocketSession session = subscriberService.getSession(req.sessionId());

    if (session != null && session.isOpen()) {
      String json = objectMapper.writeValueAsString(req.payload());
      session.sendMessage(new TextMessage(json));
    }
  }
}


