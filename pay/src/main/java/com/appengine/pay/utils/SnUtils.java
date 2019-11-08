package com.appengine.pay.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SnUtils {
    private static final int DEFAULT_LENGTH = 20;

    /**
     * 生成 微信，支付宝等第三方支付的 订单号
     *
     * @param oid
     * @return
     */
    public static String generateOrderNo(String oid) {
        return generateOrderNo(oid, DEFAULT_LENGTH);
    }

    public static String generateOrderNo(String oid, int length) {
        if (StringUtils.isBlank(oid) || oid.length() < 2) {
            throw new IllegalArgumentException("uid Illegal");
        }

        StringBuilder no = new StringBuilder();

        no.append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));

        no.append(RandomStringUtils.randomNumeric(length - no.length() - 2));

        no.append(oid.substring(oid.length() - 2));
        return no.toString();
    }

    public static void main(String[] args) {
        String re = generateOrderNo("20150806122522316");
        System.out.println(re);
        System.out.println(re.length());
    }
}
