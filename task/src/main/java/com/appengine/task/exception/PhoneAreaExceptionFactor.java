package com.appengine.task.exception;

import com.appengine.common.exception.ExcepFactor;
import com.appengine.common.utils.GlobalConstants;
import org.springframework.http.HttpStatus;

public class PhoneAreaExceptionFactor extends ExcepFactor {

    public static final PhoneAreaExceptionFactor NO_PHONE_NUMBER = new PhoneAreaExceptionFactor(HttpStatus.BAD_REQUEST, 1,
            "no phone number", "号码不存在");
    public static final PhoneAreaExceptionFactor UPLOAD_FAILED = new PhoneAreaExceptionFactor(HttpStatus.BAD_REQUEST, 7,
            "upload failed", "上传失败");
    public static final PhoneAreaExceptionFactor UPLOAD_FILE_IS_NULL = new PhoneAreaExceptionFactor(HttpStatus.BAD_REQUEST, 8,
            "upload file is null", "上传文件为空");
    public static final PhoneAreaExceptionFactor UPLOAD_FILE_TYPE_ERROR = new PhoneAreaExceptionFactor(HttpStatus.BAD_REQUEST, 9,
            "upload image type error", "图片类型错误");

    protected PhoneAreaExceptionFactor(HttpStatus httpStatus, int errorCode, String errorMsg, String errorMsgCn) {
        super(GlobalConstants.PHONEAREA, httpStatus, errorCode, errorMsg, errorMsgCn);
    }
}
