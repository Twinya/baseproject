package com.appengine.sms.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fu on 2016/1/15.
 */
public class PhoneNumber {
    private String errCode;
    private static String patternString = "^1\\d{10}$";

    public static class PhoneNumberInstance {
        private static final PhoneNumber INSTANCE = new PhoneNumber();
    }


    private PhoneNumber() {
        super();
    }

    public static PhoneNumber getInstance() {
        return PhoneNumberInstance.INSTANCE;
    }

    public boolean verify(String phoneNumber) {
        Pattern pattern = Pattern.compile(patternString);
        Matcher m = pattern.matcher(phoneNumber);
        if (!m.matches()) {
            errCode = "手机号码格式错误";
            return false;
        } else {
            errCode = "";
        }
        return true;
    }

    public String hideMobile(String mobile) {
        return mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    public String getErrCode() {
        return errCode;
    }
}
