package com.appengine.user.service;

import com.appengine.common.utils.PageHelp;
import com.appengine.user.domain.AuthOptionEnum;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

@Service
public class SmsOrIpAuthService implements ISmsOrIpHandler{
    @Resource(name = "rateRedis")
    private StringRedisTemplate redis;

    @Override
    public Boolean add(String arg, AuthOptionEnum status) {
       return redis.opsForZSet().add(status.getValue(), arg, 1);
    }

    @Override
    public Long del(String arg, AuthOptionEnum status) {
        return redis.opsForZSet().remove(status.getValue(), arg);
    }

    @Override
    public Long count(AuthOptionEnum key) {
        return redis.opsForZSet().zCard(key.getValue());
    }

    @Override
    public PageHelp<String> findAll(AuthOptionEnum key, Long start, Long end) {
        Set<String> set = redis.opsForZSet().range(key.getValue(), 0, -1);
        return new PageHelp<>(start, end, set);
    }
}
