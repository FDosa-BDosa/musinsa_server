package com.mudosa.musinsa.coupon.service;

import java.time.LocalDateTime;

public record CouponIssueMessage(
        Long userId,
        Long couponId,
        LocalDateTime issuedAt,
        LocalDateTime expiredAt
) {
}
