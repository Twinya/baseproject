package com.appengine.auth.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class VerifyCodeRedisDao {
    @Resource(name = "rateRedis")
    private StringRedisTemplate redis;
    private static final String KEY_PREFIX = "verify_code:";

    public boolean isValidCode(String requestId, String code) {
        String key = KEY_PREFIX + requestId;
        String myCode = redis.opsForValue().get(key);
        redis.delete(key);
        return code.equals(myCode);
    }

    public void setCode(String requestId, String code) {
        String key = KEY_PREFIX + requestId;
        redis.opsForValue().set(key, code);
        redis.expire(key, 5, TimeUnit.MINUTES);
    }
}
