package com.appengine.auth.redis;

import com.appengine.auth.model.Admin;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class TokenLimitRedisDao {
    @Resource(name = "rateRedis")
    private StringRedisTemplate redis;
    private static final String TOKEN_KEY_PREFIX = "token:";

    /**
     * @param uid
     * @param token 每个token 有效期为 30 天，不管是否使用30天后都会过期
     *              管理员 30 分钟无操作 token 失效
     *              普通用户7天内未登录 token 失效
     * @return
     */
    public boolean isValidToken(Long uid, String token) {
        String key = TOKEN_KEY_PREFIX + uid;
        String value = redis.opsForValue().get(key);
        if (Admin.isAdmin(uid)) {
            redis.expire(key, 30, TimeUnit.MINUTES);
        } else {
            redis.expire(key, 7, TimeUnit.DAYS);
        }
        return token.equals(value);
    }

    public void setToken(Long uid, String token) {
        String key = TOKEN_KEY_PREFIX + uid;
        redis.opsForValue().set(key, token);
        if (Admin.isAdmin(uid)) {
            redis.expire(key, 30, TimeUnit.MINUTES);
        } else {
            redis.expire(key, 7, TimeUnit.DAYS);
        }
    }

    public void removeToken(Long uid) {
        String key = TOKEN_KEY_PREFIX + uid;
        redis.delete(key);
    }
}
