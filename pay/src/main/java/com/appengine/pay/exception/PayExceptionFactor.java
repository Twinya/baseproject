package com.appengine.pay.exception;

import com.appengine.common.exception.ExcepFactor;
import com.appengine.common.utils.GlobalConstants;
import org.springframework.http.HttpStatus;

public class PayExceptionFactor extends ExcepFactor {
    public static final PayExceptionFactor PAY_FAILED = new PayExceptionFactor(HttpStatus.BAD_REQUEST, 1,
            "pay failed", "支付失败");
    public static final PayExceptionFactor ACCOUNT_EXISTS = new PayExceptionFactor(HttpStatus.BAD_REQUEST, 2,
            "account exists", "账号已存在");
    public static final PayExceptionFactor ACCOUNT_NOT_EXISTS = new PayExceptionFactor(HttpStatus.BAD_REQUEST, 3,
            "account not exists", "账号不存在");
    public static final PayExceptionFactor USERPASS_ERROR = new PayExceptionFactor(HttpStatus.BAD_REQUEST, 4,
            "password error", "密码错误");
    public static final PayExceptionFactor UPLOAD_FILE_IS_NULL = new PayExceptionFactor(HttpStatus.BAD_REQUEST, 5,
            "upload file is null", "上传文件为空");
    public static final PayExceptionFactor UPLOAD_FILE_TYPE_ERROR = new PayExceptionFactor(HttpStatus.BAD_REQUEST, 6,
            "upload image type error", "图片类型错误");
    public static final PayExceptionFactor GET_USER_INFO_FAILED = new PayExceptionFactor(HttpStatus.BAD_REQUEST, 7,
            "get user info failed", "获取用户信息失败");
    public static final PayExceptionFactor SIGN_DATA_NOT_NULL = new PayExceptionFactor(HttpStatus.BAD_REQUEST, 8,
            "sign data is null", "签名信息不能为空");

    protected PayExceptionFactor(HttpStatus httpStatus, int errorCode, String errorMsg, String errorMsgCn) {
        super(GlobalConstants.PAY, httpStatus, errorCode, errorMsg, errorMsgCn);
    }
}
