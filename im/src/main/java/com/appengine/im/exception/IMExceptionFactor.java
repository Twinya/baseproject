package com.appengine.im.exception;

import com.appengine.common.exception.ExcepFactor;
import com.appengine.common.utils.GlobalConstants;
import org.springframework.http.HttpStatus;

public class IMExceptionFactor extends ExcepFactor {
    public static final IMExceptionFactor REGIST_FAILED = new IMExceptionFactor(HttpStatus.BAD_REQUEST, 1,
            "regist failed", "注册失败");
    public static final IMExceptionFactor ACCOUNT_EXISTS = new IMExceptionFactor(HttpStatus.BAD_REQUEST, 2,
            "account exists", "账号已存在");
    public static final IMExceptionFactor ACCOUNT_NOT_EXISTS = new IMExceptionFactor(HttpStatus.BAD_REQUEST, 3,
            "account not exists", "账号不存在");
    public static final IMExceptionFactor USERPASS_ERROR = new IMExceptionFactor(HttpStatus.BAD_REQUEST, 4,
            "password error", "密码错误");
    public static final IMExceptionFactor UPLOAD_FILE_IS_NULL = new IMExceptionFactor(HttpStatus.BAD_REQUEST, 5,
            "upload file is null", "上传文件为空");
    public static final IMExceptionFactor UPLOAD_FILE_TYPE_ERROR = new IMExceptionFactor(HttpStatus.BAD_REQUEST, 6,
            "upload image type error", "图片类型错误");
    public static final IMExceptionFactor GET_USER_INFO_FAILED = new IMExceptionFactor(HttpStatus.BAD_REQUEST, 7,
            "get user info failed", "获取用户信息失败");

    protected IMExceptionFactor(HttpStatus httpStatus, int errorCode, String errorMsg, String errorMsgCn) {
        super(GlobalConstants.IM, httpStatus, errorCode, errorMsg, errorMsgCn);
    }
}
