package com.appengine.oplog.exception;

import com.appengine.common.exception.ExcepFactor;
import com.appengine.common.utils.GlobalConstants;
import org.springframework.http.HttpStatus;

public class OplogExceptionFactor extends ExcepFactor {

    public static final OplogExceptionFactor NO_PHONE_NUMBER = new OplogExceptionFactor(HttpStatus.BAD_REQUEST, 1,
            "no phone number", "号码不存在");

    protected OplogExceptionFactor(HttpStatus httpStatus, int errorCode, String errorMsg, String errorMsgCn) {
        super(GlobalConstants.OPLOG, httpStatus, errorCode, errorMsg, errorMsgCn);
    }
}
