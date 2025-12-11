package com.mudosa.musinsa.chat.service;

import com.mudosa.musinsa.chat.dto.ChatFanoutMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessagePublishService {

  private final RabbitTemplate rabbitTemplate;

  public void publishChatMessage(Long chatId, Object payload) {
    ChatFanoutMessage message = new ChatFanoutMessage(chatId, payload);

    rabbitTemplate.convertAndSend(
        "chat.fanout.exchange",    // Exchange
        "chat." + chatId,          // Routing key (optional)
        message
    );
  }
}
