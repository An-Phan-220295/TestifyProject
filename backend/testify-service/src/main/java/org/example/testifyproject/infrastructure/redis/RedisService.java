package org.example.testifyproject.infrastructure.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public void set(String key, Object value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }

    public <T> T get(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(key);
        return clazz.cast(value);
    }

    public boolean delete(String key) {
        return  redisTemplate.delete(key);
    }

    public boolean exists(String key) {
        try {
            boolean exists = redisTemplate.hasKey(key);
            log.debug("Redis EXISTS key={} => {}", key, exists);
            return exists;
        } catch (Exception e) {
            log.error("Failed to check existence of Redis key {}: {}", key, e.getMessage());
            return false;
        }
    }

    public Duration checkTTL(String key, TimeUnit unit) {
        long ttl = redisTemplate.getExpire(key, unit);

        if (ttl < 0) {
            return null;
        }

        return Duration.of(ttl, unit.toChronoUnit());
    }

    public boolean setIfAbsent(String key, Object value, Duration ttl) {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(key, value, ttl);
        return Boolean.TRUE.equals(result);
    }

    public long incrementWithTTL(String key, Duration ttl) {
        long value = redisTemplate.opsForValue().increment(key);
        if (value == 1L) {
            redisTemplate.expire(key, ttl);
        }
        return value;
    }
}
