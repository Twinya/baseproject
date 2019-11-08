package com.appengine.task.rest;

import com.appengine.auth.annotation.AuthType;
import com.appengine.auth.annotation.BaseInfo;
import com.appengine.task.domain.PhoneArea;
import com.appengine.task.service.PhoneAreaService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/phonearea")
public class PhoneAreaController {
    @Resource
    private PhoneAreaService phoneAreaService;

    @GetMapping(value = "/phone/{phone}")
    @BaseInfo(needAuth = AuthType.OPTION)
    public PhoneArea get(@PathVariable("phone") String phone) {
        return phoneAreaService.getPhoneArea(phone);
    }
}
