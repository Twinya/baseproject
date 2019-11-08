package com.appengine.sms.exception;

import com.appengine.common.exception.ExcepFactor;
import com.appengine.common.utils.GlobalConstants;
import org.springframework.http.HttpStatus;

public class SMSExceptionFactor extends ExcepFactor {

    public static final SMSExceptionFactor NO_SIGN = new SMSExceptionFactor(HttpStatus.BAD_REQUEST, 1,
            "sign not exist", "签名不存在");
    public static final SMSExceptionFactor SIGN_EXIST = new SMSExceptionFactor(HttpStatus.BAD_REQUEST, 2,
            "sign already exist", "签名已存在");
    public static final SMSExceptionFactor WRONG_NUMBER = new SMSExceptionFactor(HttpStatus.BAD_REQUEST, 3,
            "wrong number", "手机号码格式错误");

    public static final SMSExceptionFactor VERIFY_FAILED = new SMSExceptionFactor(HttpStatus.BAD_REQUEST, 4,
            "image code error", "图形验证码错误");
    public static final SMSExceptionFactor FAILED = new SMSExceptionFactor(HttpStatus.BAD_REQUEST, 5,
            "send failed", "发送失败");
    public static final SMSExceptionFactor NO_ISP = new SMSExceptionFactor(HttpStatus.BAD_REQUEST, 6,
            "ISP not exist", "短信运营商不存在");

    protected SMSExceptionFactor(HttpStatus httpStatus, int errorCode, String errorMsg, String errorMsgCn) {
        super(GlobalConstants.SMS, httpStatus, errorCode, errorMsg, errorMsgCn);
    }
}
