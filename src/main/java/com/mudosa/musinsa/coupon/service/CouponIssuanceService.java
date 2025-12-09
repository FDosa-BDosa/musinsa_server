package com.mudosa.musinsa.coupon.service;

import com.mudosa.musinsa.coupon.model.Coupon;
import com.mudosa.musinsa.coupon.presentation.dto.res.CouponIssuanceResDto;
import com.mudosa.musinsa.coupon.repository.CouponRepository;
import com.mudosa.musinsa.coupon.repository.MemberCouponRepository;
import com.mudosa.musinsa.exception.BusinessException;
import com.mudosa.musinsa.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponIssuanceService {

    private static final String ISSUED_SET_PREFIX = "coupon:issued:";
    private static final String STOCK_KEY_PREFIX = "coupon:stock:";

    private final CouponRepository couponRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final StringRedisTemplate redisTemplate;
    private final CouponIssueLuaScript couponIssueLuaScript;
    private final CouponIssueQueueProducer couponIssueQueueProducer;

    // 조회 전용 메서드
    public Optional<CouponIssuanceResDto> findIssuedCoupon(Long userId, Long couponId) {
        return memberCouponRepository.findByUserIdAndCouponId(userId, couponId)
                .map(existing -> CouponIssuanceResDto.duplicate(
                        existing.getId(),
                        couponId,
                        existing.getExpiredAt(),
                        existing.getCreatedAt()
                ));
    }

    public long countIssuedByUser(Long userId, Long couponId) {
        return memberCouponRepository.countByUserIdAndCouponId(userId, couponId);
    }

    /**
     * Redis Lua 스크립트 기반 쿠폰 발급 (분산락 없이 원자 실행).
     * - Redis 내부에서 중복 체크, 재고 차감, TTL 설정을 단일 명령으로 처리
     * - 성공 시 비동기 큐에 적재하여 DB는 별도 워커가 저장
     * - 분산락을 추가하지 않아도 Redis 단일 명령으로 임계구역이 보호되므로 지연을 최소화
     */
    public CouponIssuanceResDto issueCoupon(Long userId, Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COUPON_NOT_FOUND));

        validateIssuableWindow(coupon);

        String issueKey = ISSUED_SET_PREFIX + couponId;
        String stockKey = STOCK_KEY_PREFIX + couponId;

        long ttlMillis = calculateIssueKeyTtl(coupon);

        Long result = couponIssueLuaScript.execute(
                redisTemplate,
                List.of(issueKey, stockKey),
                userId.toString(),
                String.valueOf(ttlMillis)
        );

        if (result == null) {
            throw new BusinessException(
                    ErrorCode.COUPON_APPLIED_FALIED,
                    "쿠폰 발급 처리 중 오류가 발생했습니다."
            );
        }

        return switch (result.intValue()) {
            case 1 -> handleIssueSuccess(userId, coupon, ttlMillis);
            case 0 -> handleDuplicate(userId, coupon);
            case -1 -> throw new BusinessException(ErrorCode.COUPON_OUT_OF_STOCK);
            default -> throw new BusinessException(
                    ErrorCode.COUPON_APPLIED_FALIED,
                    "알 수 없는 쿠폰 발급 결과입니다."
            );
        };
    }

    private CouponIssuanceResDto handleIssueSuccess(Long userId, Coupon coupon, long ttlMillis) {
        LocalDateTime issuedAt = LocalDateTime.now();
        LocalDateTime expiredAt = coupon.getEndDate();

        couponIssueQueueProducer.enqueue(
                new CouponIssueMessage(userId, coupon.getId(), issuedAt, expiredAt)
        );

        log.info("쿠폰 발급 큐 적재 완료 - userId: {}, couponId: {}, ttlMillis: {}",
                userId, coupon.getId(), ttlMillis);

        return CouponIssuanceResDto.issued(null, coupon.getId(), expiredAt, issuedAt);
    }

    private CouponIssuanceResDto handleDuplicate(Long userId, Coupon coupon) {
        Optional<CouponIssuanceResDto> existing = findIssuedCoupon(userId, coupon.getId());

        if (existing.isPresent()) {
            return existing.get();
        }

        log.warn("Redis에서 중복 감지되었으나 DB 기록 없음 - userId: {}, couponId: {}", userId, coupon.getId());
        return CouponIssuanceResDto.duplicate(null, coupon.getId(), coupon.getEndDate(), null);
    }

    private long calculateIssueKeyTtl(Coupon coupon) {
        LocalDateTime now = LocalDateTime.now();
        if (coupon.getEndDate() == null || now.isAfter(coupon.getEndDate())) {
            return 0L;
        }

        Duration duration = Duration.between(now, coupon.getEndDate());
        long millis = duration.toMillis();
        // 최소 1분 TTL 보장
        return Math.max(millis, Duration.ofMinutes(1).toMillis());
    }

    private void validateIssuableWindow(Coupon coupon) {
        LocalDateTime now = LocalDateTime.now();
        if (!Boolean.TRUE.equals(coupon.getIsActive())) {
            throw new BusinessException(ErrorCode.COUPON_APPLIED_FALIED, "비활성화된 쿠폰입니다.");
        }
        if (now.isBefore(coupon.getStartDate()) || now.isAfter(coupon.getEndDate())) {
            throw new BusinessException(ErrorCode.COUPON_EXPIRED, "쿠폰 발급 가능 기간이 아닙니다");
        }
    }
}
