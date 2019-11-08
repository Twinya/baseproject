package com.appengine.task.rest;

import com.appengine.auth.annotation.AuthType;
import com.appengine.auth.annotation.BaseInfo;
import com.appengine.task.domain.HomeAd;
import com.appengine.task.service.HomeAdService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/ad")
public class HomeAdController {

    @Resource
    HomeAdService homeAdService;


    @BaseInfo(needAuth = AuthType.OPTION)
    @GetMapping(value = "")
    public HomeAd get(@RequestParam(required = false, defaultValue = "") String app) {
        return homeAdService.getByApp(app);
    }

}
