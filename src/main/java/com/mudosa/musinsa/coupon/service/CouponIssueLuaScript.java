package com.mudosa.musinsa.coupon.service;

import lombok.Getter;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
public class CouponIssueLuaScript {

    private final DefaultRedisScript<Long> issueScript;

    public CouponIssueLuaScript() {
        this.issueScript = new DefaultRedisScript<>();
        this.issueScript.setResultType(Long.class);
        this.issueScript.setScriptText(buildScript());
    }

    public Long execute(StringRedisTemplate redisTemplate, List<String> keys, String... args) {
        return redisTemplate.execute(issueScript, keys, (Object[]) args);
    }

    private String buildScript() {
        return """
            -- KEYS[1]: 발급 여부 Set 키
            -- KEYS[2]: 재고 카운터 키
            -- ARGV[1]: userId
            -- ARGV[2]: 발급 Set TTL (millis). 0 이하면 TTL 미설정
            if redis.call('SISMEMBER', KEYS[1], ARGV[1]) == 1 then
              return 0
            end

            local stock = tonumber(redis.call('GET', KEYS[2]) or '0')
            if stock <= 0 then
              return -1
            end

            redis.call('DECR', KEYS[2])
            redis.call('SADD', KEYS[1], ARGV[1])

            local ttl = tonumber(ARGV[2] or '0')
            if ttl > 0 then
              redis.call('PEXPIRE', KEYS[1], ttl)
            end

            return 1
            """;
    }
}
