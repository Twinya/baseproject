package com.appengine.frame.spring;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Nonnull;

/**
 * json统一拦截处理模板
 * <p>
 * Authors: fuyou
 * Version: 1.0  Created at 2015-10-03 00:15.
 */
@Order(1)
@ControllerAdvice(basePackages = "com.appengine")
public class JsonResultValueHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(@Nonnull MethodParameter returnType, @Nonnull Class converterType) {
        return FastJsonHttpMessageConverter.class.isAssignableFrom(converterType);
    }

    @Override
    public Object beforeBodyWrite(Object body, @Nonnull MethodParameter returnType, @Nonnull MediaType selectedContentType, @Nonnull Class selectedConverterType, @Nonnull ServerHttpRequest request, @Nonnull ServerHttpResponse response) {
        //json保证apistatus在最前面
        JSONObject result = new JSONObject(true);

        if (body == null) {
            body = new JSONObject();
        }

        if (StringUtils.endsWith(((ServletServerHttpRequest) request).getServletRequest().getServletPath(), "Notify")) {
            return body;
        }
        JSONObject data = new JSONObject();
        if (StringUtils.equals(((ServletServerHttpRequest) request).getServletRequest().getServletPath(), "/error")) {
            result.put("code", 1);
            if (body instanceof String) {
                body = JSON.parse((String) body);
            } else {
                body = JSON.parse(body.toString());
            }
            result.put("message", body);
        } else {
            result.put("code", 0);
            data.put("data", body);
            result.put("message", data);
        }
        return result;
    }
}
