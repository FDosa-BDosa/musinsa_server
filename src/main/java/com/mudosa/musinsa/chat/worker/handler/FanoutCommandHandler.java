package com.mudosa.musinsa.chat.worker.handler;

import com.mudosa.musinsa.chat.service.RedisSubscriberService;
import com.mudosa.musinsa.chat.websocket.FanoutExecutor;
import com.mudosa.musinsa.chat.worker.dto.ChatFanoutMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FanoutCommandHandler {

  private final RedisSubscriberService subscriberService;
  private final FanoutExecutor fanoutExecutor;

  public void handle(ChatFanoutMessage message) {

    Long chatId = message.chatId();
    Object payload = message.payload();

    var sessionIds = subscriberService.getSubscribers(chatId);

    fanoutExecutor.execute(sessionIds, payload);
  }
}
