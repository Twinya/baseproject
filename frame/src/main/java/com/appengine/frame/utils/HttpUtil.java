package com.appengine.frame.utils;

import com.alibaba.druid.support.json.JSONUtils;
import com.squareup.okhttp.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("Duplicates")
public class HttpUtil {
    public static final MediaType MEDIA_TYPE_ANY = MediaType.parse("*/*; charset=utf-8");
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");


    public static Response doPost(String url, String body) {
        Request request = getBuilder().url(url).post(RequestBody.create(MEDIA_TYPE_ANY, body)).build();
        try {
            return OkHttpUtil.execute(request);
        } catch (IOException e) {

        }
        return null;
    }

    public static Response doPost(String url, Map<String, String> form) {
        Request request;
        if (form == null) {
            return null;
        }
        if (form.size() == 0) {
            request = getBuilder().url(url).build();
        } else {
            request = getBuilder().url(url).post(getFormBody(form)).build();
        }
        try {
            return OkHttpUtil.execute(request);
        } catch (IOException e) {

        }
        return null;
    }

    public static Response doJsonPost(String url, Map<String, String> form) {
        Request request;
        if (form == null) {
            return null;
        }
        if (form.size() == 0) {
            request = getBuilder().url(url).build();
        } else {
            request = getBuilder().url(url).post(RequestBody.create(MEDIA_TYPE_JSON, JSONUtils.toJSONString(form))).build();
        }
        try {
            return OkHttpUtil.execute(request);
        } catch (IOException e) {

        }
        return null;
    }

    private static RequestBody getFormBody(Map<String, String> form) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        form.forEach((key, value) -> {
            if (key != null) {
                builder.add(key, value);
            }
        });
        return builder.build();
    }

    public static Response get(String url, LinkedHashMap<String, String> params) {
        String requestUrl = attachHttpGetParams(url, params);
        Request request = getBuilder().url(requestUrl).build();
        try {
            return OkHttpUtil.execute(request);
        } catch (IOException e) {
        }
        return null;
    }

    public static Response get(String url) {
        Request request = getBuilder().url(url).build();
        try {
            return OkHttpUtil.execute(request);
        } catch (IOException e) {
        }
        return null;
    }

    private static Request.Builder getBuilder() {
        Request.Builder builder = new Request.Builder();
        Headers.Builder hBuilder = new Headers.Builder();
        builder.headers(hBuilder.build());
        return builder;
    }

    public static String attachHttpGetParams(String url, LinkedHashMap<String, String> params) {
        Iterator<String> keys = params.keySet().iterator();
        Iterator<String> values = params.values().iterator();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("?");

        for (int i = 0; i < params.size(); i++) {
            String value = null;
            try {
                value = URLEncoder.encode(values.next(), "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            stringBuffer.append(keys.next() + "=" + value);
            if (i != params.size() - 1) {
                stringBuffer.append("&");
            }
        }
        return url + stringBuffer.toString();
    }
}
