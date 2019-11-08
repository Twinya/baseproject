package com.appengine.sms.utils;

public class RandomCode {

    public static String getCode() {
        return (int) ((Math.random() * 9 + 1) * 1000) + "";
    }

    public static String getCode(int length) {
        if (length <= 0) {
            return "";
        }
        if (length > 8) {
            length = 8;
        }
        int l = 1;
        for (int i = 0; i < length; i++) {
            l *= 10;
        }
        return (int) ((Math.random() * 9 + 1) * l) + "";
    }
}
