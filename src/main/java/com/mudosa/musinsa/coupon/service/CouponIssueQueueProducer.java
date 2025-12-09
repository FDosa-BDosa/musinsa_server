package com.mudosa.musinsa.coupon.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mudosa.musinsa.exception.BusinessException;
import com.mudosa.musinsa.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
@Slf4j
public class CouponIssueQueueProducer {

    private static final String ISSUE_QUEUE_KEY = "coupon:issue:queue";

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public void enqueue(CouponIssueMessage message) {
        try {
            String payload = objectMapper.writeValueAsString(message);
            redisTemplate.opsForList().leftPush(ISSUE_QUEUE_KEY, payload);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.COUPON_APPLIED_FALIED, "쿠폰 발급 큐 직렬화에 실패했습니다.");
        }
    }

    public CouponIssueMessage poll(Duration timeout) {
        String payload = redisTemplate.opsForList().rightPop(ISSUE_QUEUE_KEY, timeout);
        if (payload == null) {
            return null;
        }
        try {
            return objectMapper.readValue(payload, CouponIssueMessage.class);
        } catch (JsonProcessingException e) {
            log.error("쿠폰 발급 큐 역직렬화 실패 - payload: {}", payload, e);
            return null;
        }
    }
}
