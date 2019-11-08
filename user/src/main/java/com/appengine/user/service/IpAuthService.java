package com.appengine.user.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

@Service
public class IpAuthService {
    public static final String WHITE = "auth:ipwhite";
    public static final String BLACK = "ipblack";
    @Resource(name = "rateRedis")
    private StringRedisTemplate redis;

    public Boolean addBlack(String ip) {
        return redis.opsForZSet().add(BLACK, ip, 1);
    }

    public Boolean addWhite(String ip) {
        return redis.opsForZSet().add(WHITE, ip, 1);
    }

    public Long delBlack(Object... ip) {
        return redis.opsForZSet().remove(BLACK, ip);
    }

    public Long delWhite(Object... ip) {
        return redis.opsForZSet().remove(WHITE, ip);
    }

    public Long count(String key) {
        return redis.opsForZSet().zCard(key);
    }

    public Set<String> get(String key, Long start, Long end) {
        return redis.opsForZSet().range(key, start, end);
    }


}
