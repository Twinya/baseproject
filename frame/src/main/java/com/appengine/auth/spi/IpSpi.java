package com.appengine.auth.spi;


import com.appengine.auth.model.AuthExcepFactor;
import com.appengine.auth.model.AuthException;
import com.appengine.auth.model.AuthRequest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author jolestar@gmail.com
 */
@Component("IpSpi")
public class IpSpi extends AbstractAuthSpi {

    public static final String SPI_NAME = "Ip";
    public static final String WHITE = "ipwhite";
    public static final String BLACK = "ipblack";
    @Resource(name = "rateRedis")
    private StringRedisTemplate redis;

    @Override
    public String getName() {
        return SPI_NAME;
    }

    @Override
    protected boolean checkCanAuth(AuthRequest request) {
        Double a = redis.opsForZSet().score(WHITE, request.getRemoteIp());
        Double b = redis.opsForZSet().score(BLACK, request.getRemoteIp());
        return a != null || b != null;
    }

    @Override
    public long auth(AuthRequest request) throws AuthException {
        if (redis.opsForZSet().score(BLACK, request.getRemoteIp()) != null) {
            throw new AuthException(AuthExcepFactor.E_USER_AUTHFAIL, "ip blocked");
        } else {
            return 0L;
        }
    }

}
