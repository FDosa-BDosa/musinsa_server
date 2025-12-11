package com.mudosa.musinsa.chat.worker.dto;


public record ChatFanoutMessage(Long chatId, Object payload) {
}
