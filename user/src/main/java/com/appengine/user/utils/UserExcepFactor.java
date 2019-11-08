package com.appengine.user.utils;


import com.appengine.common.exception.ExcepFactor;
import com.appengine.common.utils.GlobalConstants;
import org.springframework.http.HttpStatus;

public class UserExcepFactor extends ExcepFactor {

    public static final UserExcepFactor ACCOUNT_EXISTS = new UserExcepFactor(HttpStatus.BAD_REQUEST, 1,
            "account exists", "账号已存在");
    public static final UserExcepFactor ACCOUNT_NOT_EXISTS = new UserExcepFactor(HttpStatus.BAD_REQUEST, 2,
            "account not exists", "账号不存在");
    public static final UserExcepFactor USERPASS_ERROR = new UserExcepFactor(HttpStatus.BAD_REQUEST, 3,
            "password error", "密码错误");
    public static final UserExcepFactor FEEDBACK_ERROR = new UserExcepFactor(HttpStatus.BAD_REQUEST, 4,
            "feed error", "反馈失败");
    public static final UserExcepFactor MOBILE_NUMBER_ERROR = new UserExcepFactor(HttpStatus.BAD_REQUEST, 5,
            "mobile number error", "手机号码格式错误");
    public static final UserExcepFactor TYPE_ERROR = new UserExcepFactor(HttpStatus.BAD_REQUEST, 6,
            "type error", "type错误");
    public static final UserExcepFactor IDCARD_ERROR = new UserExcepFactor(HttpStatus.BAD_REQUEST, 7,
            "id card format error", "身份证格式错误");
    public static final UserExcepFactor BANKCARD_ERROR = new UserExcepFactor(HttpStatus.BAD_REQUEST, 8,
            "bank card format error", "银行卡格式错误");
    public static final UserExcepFactor NOT_AUTHED = new UserExcepFactor(HttpStatus.BAD_REQUEST, 9,
            "not authed", "还没有认证");

    protected UserExcepFactor(HttpStatus httpStatus, int errorCode, String errorMsg, String errorMsgCn) {
        super(GlobalConstants.USER_ID_AUTH, httpStatus, errorCode, errorMsg, errorMsgCn);
    }

}
