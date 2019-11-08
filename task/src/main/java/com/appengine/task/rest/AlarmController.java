package com.appengine.task.rest;

import com.appengine.auth.annotation.AuthType;
import com.appengine.auth.annotation.BaseInfo;
import com.appengine.task.service.AlarmService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/alarm")
public class AlarmController {

    @Resource
    AlarmService alarmService;

    @GetMapping(value = "")
    @BaseInfo(needAuth = AuthType.REQUIRED)
    public double get() {
        return alarmService.getSmsSuccessRate();
    }
}
