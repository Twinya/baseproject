package com.appengine.user.utils;


import com.appengine.common.exception.ExcepFactor;
import com.appengine.common.utils.GlobalConstants;
import org.springframework.http.HttpStatus;

public class BillExcepFactor extends ExcepFactor {

    public static final BillExcepFactor ACCOUNT_EXISTS = new BillExcepFactor(HttpStatus.BAD_REQUEST, 1,
            "account exists", "账号已存在");
    public static final BillExcepFactor ACCOUNT_NOT_EXISTS = new BillExcepFactor(HttpStatus.BAD_REQUEST, 2,
            "account not exists", "账号不存在");

    protected BillExcepFactor(HttpStatus httpStatus, int errorCode, String errorMsg, String errorMsgCn) {
        super(GlobalConstants.BILL, httpStatus, errorCode, errorMsg, errorMsgCn);
    }

}
