package com.mudosa.musinsa.chat.worker.listener;


import com.mudosa.musinsa.chat.worker.dto.ChatFanoutMessage;
import com.mudosa.musinsa.chat.worker.handler.FanoutCommandHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatMessageWorkerListener {

  private final FanoutCommandHandler handler;

  @RabbitListener(queues = "chat.message.q", containerFactory = "rabbitListenerContainerFactory")
  public void onMessage(ChatFanoutMessage message) {

    log.info("[Worker] Consume chatId={} payload={}", message.chatId(), message.payload());

    handler.handle(message);
  }
}
