package com.appengine.task.rest;

import com.appengine.auth.annotation.AuthType;
import com.appengine.auth.annotation.BaseInfo;
import com.appengine.task.domain.Toutiao;
import com.appengine.task.service.ToutiaoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/toutiao")
public class ToutiaoController {

    @Resource
    ToutiaoService toutiaoService;

    @GetMapping(value = "")
    @BaseInfo(needAuth = AuthType.OPTION)
    public Page<Toutiao> get(@RequestParam(required = false, defaultValue = "1") int page,
                             @RequestParam(required = false, defaultValue = "100") int pageSize) {
        PageRequest request = PageRequest.of(page - 1, pageSize);
        return toutiaoService.getList(request);
    }
}
