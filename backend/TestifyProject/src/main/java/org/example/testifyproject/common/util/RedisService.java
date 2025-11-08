package org.example.testifyproject.common.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

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

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public boolean exists(String key) {
        try {
            boolean exists = redisTemplate.hasKey(key);
            log.debug("🔍 Redis EXISTS key={} => {}", key, exists);
            return exists;
        } catch (Exception e) {
            log.error("❌ Failed to check existence of Redis key {}: {}", key, e.getMessage());
            return false;
        }
    }
}
