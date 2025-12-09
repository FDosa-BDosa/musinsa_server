package com.mudosa.musinsa.coupon.service;

import com.mudosa.musinsa.coupon.model.Coupon;
import com.mudosa.musinsa.coupon.model.MemberCoupon;
import com.mudosa.musinsa.coupon.repository.CouponRepository;
import com.mudosa.musinsa.coupon.repository.MemberCouponRepository;
import com.mudosa.musinsa.exception.BusinessException;
import com.mudosa.musinsa.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class CouponIssueQueueWorker {

    private final CouponIssueQueueProducer queueProducer;
    private final CouponRepository couponRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final TransactionTemplate transactionTemplate;

    @Scheduled(fixedDelay = 200)
    public void consume() {
        CouponIssueMessage message = queueProducer.poll(Duration.ofMillis(200));
        while (message != null) {
            persist(message);
            message = queueProducer.poll(Duration.ZERO);
        }
    }

    private void persist(CouponIssueMessage message) {
        transactionTemplate.executeWithoutResult(status -> {
            try {
                Coupon coupon = couponRepository.findByIdForUpdate(message.couponId())
                        .orElseThrow(() -> new BusinessException(ErrorCode.COUPON_NOT_FOUND));

                coupon.validateIssuable(LocalDateTime.now());
                coupon.increaseIssuedQuantity();

                MemberCoupon memberCoupon = MemberCoupon.issue(message.userId(), coupon);
                memberCouponRepository.save(memberCoupon);

                log.info("쿠폰 발급 DB 반영 완료 - userId: {}, couponId: {}", message.userId(), message.couponId());
            } catch (DataIntegrityViolationException e) {
                log.warn("쿠폰 발급 DB 저장 중 중복 감지 - userId: {}, couponId: {}", message.userId(), message.couponId());
            }
        });
    }
}
