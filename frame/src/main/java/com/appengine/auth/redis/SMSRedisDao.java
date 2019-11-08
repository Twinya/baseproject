package com.appengine.auth.redis;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class SMSRedisDao {
    @Resource(name = "rateRedis")
    private StringRedisTemplate redis;
    private static final String SMS_KEY_PREFIX = "sms:";

    public boolean isValidCode(String requestId, String mobile, String code) {
        String validString = requestId + ":" + code;
        String key = SMS_KEY_PREFIX + mobile;
        return redis.execute((RedisConnection redisConnection) -> {
            String rt = null;
            byte[] codebytes = redisConnection.get(key.getBytes());
            if (codebytes != null) {
                rt = new String(codebytes);
            }
            if (rt != null) {
                redisConnection.del(key.getBytes());
            }
            return validString.equals(rt);
        });
    }

    public String contains(String mobile) {
        String key = SMS_KEY_PREFIX + mobile;
        String value = redis.opsForValue().get(key);
        if (value == null) {
            return null;
        } else {
           return value.split(":")[1];
        }
    }

    public void setSMS(String mobile, String requestId, String code) {
        String key = SMS_KEY_PREFIX + mobile;
        String value = requestId + ":" + code;
        redis.execute((RedisConnection redisConnection) -> {
            redisConnection.set(key.getBytes(), value.getBytes(), Expiration.from(5, TimeUnit.MINUTES), RedisStringCommands.SetOption.UPSERT);
            return 0;
        });
    }
}
