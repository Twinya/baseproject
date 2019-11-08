package com.appengine.auth.annotation;

/**
 * @author fuyou
 * @version 1.0 Created at: 2015-10-08 12:03
 */
public @interface RateLimitTypeConfig {
    RateLimitType value();

    RateLimitRateConfig[] rates();
}
