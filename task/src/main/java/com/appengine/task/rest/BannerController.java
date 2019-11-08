package com.appengine.task.rest;

import com.appengine.auth.annotation.AuthType;
import com.appengine.auth.annotation.BaseInfo;
import com.appengine.oss.UploadService;
import com.appengine.task.domain.Banner;
import com.appengine.task.service.BannerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Authors: fuyou
 * Version: 1.0  Created at 2015-10-12 00:18.
 */
@RestController
@RequestMapping("/api/banner")
public class BannerController {

    @Resource
    private BannerService bannerService;
    @Resource
    private UploadService uploadService;

    @BaseInfo(needAuth = AuthType.OPTION)
    @GetMapping(value = "/list")
    public Page<Banner> list(
            @RequestParam(required = false, defaultValue = "") String app,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(value = "pagesize", required = false, defaultValue = "10") int pageSize
    ) {
        PageRequest request = PageRequest.of(page - 1, pageSize);
        return bannerService.getAllBanner(request, app);
    }

    @BaseInfo(needAuth = AuthType.OPTION)
    @GetMapping(value = "/list/all")
    public Page<Banner> lists(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(value = "pagesize", required = false, defaultValue = "10") int pageSize
    ) {
        PageRequest request = PageRequest.of(page - 1, pageSize);
        return bannerService.getAllBanner(request);
    }
}
