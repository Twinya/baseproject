package com.appengine.user.utils;


import com.appengine.common.exception.ExcepFactor;
import com.appengine.common.utils.GlobalConstants;
import org.springframework.http.HttpStatus;

public class ChargeExcepFactor extends ExcepFactor {

    public static final ChargeExcepFactor ACCOUNT_EXISTS = new ChargeExcepFactor(HttpStatus.BAD_REQUEST, 1,
            "account exists", "账号已存在");
    public static final ChargeExcepFactor ACCOUNT_NOT_EXISTS = new ChargeExcepFactor(HttpStatus.BAD_REQUEST, 2,
            "account not exists", "账号不存在");
    public static final ChargeExcepFactor AMOUNT_ERROR = new ChargeExcepFactor(HttpStatus.BAD_REQUEST, 3,
            "amount error", "充值金额错误");
    public static final ChargeExcepFactor REDUCE_ERROR = new ChargeExcepFactor(HttpStatus.BAD_REQUEST, 4,
            "bill error", "扣款失败");

    protected ChargeExcepFactor(HttpStatus httpStatus, int errorCode, String errorMsg, String errorMsgCn) {
        super(GlobalConstants.CHARGE, httpStatus, errorCode, errorMsg, errorMsgCn);
    }

}
