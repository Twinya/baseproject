package com.appengine.auth.spi;

import com.appengine.auth.model.AuthExcepFactor;
import com.appengine.auth.model.AuthException;
import com.appengine.auth.model.AuthRequest;
import com.appengine.frame.utils.log.ApiLogger;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author fuyou
 */
@Component("TokenSpi")
public class TokenSpi extends AbstractAuthSpi {

    @Resource(name = "rateRedis")
    private StringRedisTemplate redis;
    public static final String AUTH_HEADER_OTHER = "Token";
    public static final String AUTH_PARAM = "Token";
    private static final String SPI_NAME = "Token";

    @Override
    public String getName() {
        return SPI_NAME;
    }

    @Override
    protected boolean checkCanAuth(AuthRequest request) {
        String authHeader = StringUtils.isBlank(request.getHeader(AUTH_HEADER)) ? request.getHeader(AUTH_HEADER_OTHER) : request.getHeader(AUTH_HEADER);
        if (!StringUtils.isBlank(authHeader) && (authHeader.length() == 13 || authHeader.length() == 23)) {
            if (ApiLogger.isDebugEnabled()) {
                ApiLogger.debug("find mauth parameter in header:" + authHeader);
            }
            return true;
        }
        return false;
    }

    @Override
    public long auth(AuthRequest request) throws AuthException {
        String authHeader = StringUtils.isBlank(request.getHeader(AUTH_HEADER)) ? request.getHeader(AUTH_HEADER_OTHER) : request.getHeader(AUTH_HEADER);
        if (StringUtils.isBlank(authHeader)) {
            authHeader = request.getParameter(AUTH_PARAM);
        }
        if (redis.hasKey(authHeader)) {
            return 0;
        } else {
            throw new AuthException(AuthExcepFactor.E_USER_AUTHFAIL);
        }
    }

    @Override
    public void afterAuth(long uid, AuthRequest request) {
        super.afterAuth(uid, request);
    }
}
