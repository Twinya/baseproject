package com.appengine.task.rest;

import com.appengine.auth.annotation.AuthType;
import com.appengine.auth.annotation.BaseInfo;
import com.appengine.task.domain.AppVersion;
import com.appengine.task.service.AppVersionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/version")
public class AppVersionController {

    @Resource
    AppVersionService appVersionService;

    @GetMapping(value = "")
    @BaseInfo(needAuth = AuthType.OPTION)
    public AppVersion get(@RequestParam String app, @RequestParam String platform) {
        return appVersionService.getVersion(app, platform);
    }

    @GetMapping(value = "list")
    @BaseInfo(needAuth = AuthType.REQUIRED)
    public Page<AppVersion> getAll(@RequestParam(required = false, defaultValue = "1") int page,
                                   @RequestParam(value = "pagesize", required = false, defaultValue = "10") int pageSize
    ) {
        PageRequest request = PageRequest.of(page - 1, pageSize);
        return appVersionService.getAll(request);
    }


    @PostMapping(value = "")
    @BaseInfo(needAuth = AuthType.REQUIRED)
    public AppVersion add(@RequestParam String app,
                          @RequestParam String platform,
                          @RequestParam String url,
                          @RequestParam Integer version,
                          @RequestParam Boolean forceUpdate) {
        return appVersionService.add(app, platform, url, version, forceUpdate);
    }

    @PutMapping(value = "")
    @BaseInfo(needAuth = AuthType.REQUIRED)
    public AppVersion edit(@RequestParam Long id,
                           @RequestParam String url,
                           @RequestParam Integer version,
                           @RequestParam Boolean update) {
        return appVersionService.edit(id, url, version, update);
    }

}
