package com.appengine.task.utils;


import com.appengine.common.exception.ExcepFactor;
import com.appengine.common.utils.GlobalConstants;
import org.springframework.http.HttpStatus;

public class TaskExcepFactor extends ExcepFactor {

    public static final TaskExcepFactor NOT_EXISTS = new TaskExcepFactor(HttpStatus.BAD_REQUEST, 1,
            "not exists", "不存在");
    public static final TaskExcepFactor VERSION_NOT_EXIST = new TaskExcepFactor(HttpStatus.BAD_REQUEST, 2,
            "version not exist", "版本不存在");
    public static final TaskExcepFactor VERSION_EXIST = new TaskExcepFactor(HttpStatus.BAD_REQUEST, 3,
            "version exist", "版本已存在");

    protected TaskExcepFactor(HttpStatus httpStatus, int errorCode, String errorMsg, String errorMsgCn) {
        super(GlobalConstants.TASK_ID_AUTH, httpStatus, errorCode, errorMsg, errorMsgCn);
    }

}
