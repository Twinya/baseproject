package com.appengine.task.rest;

import com.appengine.auth.annotation.AuthType;
import com.appengine.auth.annotation.BaseInfo;
import com.appengine.task.domain.AppConfig;
import com.appengine.task.service.AppConfigService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/config")
public class AppConfigController {

    @Resource
    AppConfigService configService;

    @GetMapping(value = "")
    @BaseInfo(needAuth = AuthType.OPTION)
    public AppConfig get(@RequestParam(defaultValue = "", required = false) String app) {
        return configService.getConfig(app);
    }
}
