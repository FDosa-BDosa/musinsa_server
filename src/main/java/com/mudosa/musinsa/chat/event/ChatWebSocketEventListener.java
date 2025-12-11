package com.mudosa.musinsa.chat.event;

import com.mudosa.musinsa.chat.service.ChatMessagePublishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatWebSocketEventListener {

  private final ChatMessagePublishService publisher;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleBroadcast(ChatBroadcastEvent event) {

    // Worker가 이 메시지를 fan-out 하게 됨
    publisher.publishChatMessage(event.chatId(), event.payload());

    log.debug("[WSEvent] 메시지 큐 전송 -> chatId={}, payload={}", event.chatId(), event.payload());
  }
}
