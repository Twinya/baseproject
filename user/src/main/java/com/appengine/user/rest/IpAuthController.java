package com.appengine.user.rest;

import com.appengine.auth.annotation.AuthType;
import com.appengine.auth.annotation.BaseInfo;
import com.appengine.common.utils.PageHelp;
import com.appengine.frame.context.RequestContext;
import com.appengine.user.domain.AuthOptionEnum;
import com.appengine.user.service.SmsOrIpAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

/**
 * Authors: fuyou
 * Version: 1.0  Created at 2015-10-02 22:08.
 */
@Api(tags = "ip 黑白名单")
@Validated
@RestController
@RequestMapping("/api/ip")
public class IpAuthController {

    @Resource
    private SmsOrIpAuthService smsOrIpAuthService;

    @ApiOperation(value = "添加黑白名单")
    @BaseInfo(needAuth = AuthType.REQUIRED)
    @PostMapping(value = "/blackorwhite")
    public Boolean blackorwhite(@ApiIgnore RequestContext rc,
                                @RequestParam String arg,
                                @RequestParam AuthOptionEnum status) {
        return smsOrIpAuthService.add(arg, status);
    }

    @BaseInfo(needAuth = AuthType.REQUIRED)
    @ApiOperation(value = "删除黑白名单")
    @DeleteMapping(value = "/blackorwhite")
    public Long white(@ApiIgnore RequestContext rc,
                      @RequestParam String arg,
                      @RequestParam AuthOptionEnum status) {
        return smsOrIpAuthService.del(arg, status);
    }

    @ApiOperation(value = "黑白名单count")
    @BaseInfo(needAuth = AuthType.REQUIRED)
    @GetMapping(value = "/blackorwhite")
    public Long blackorwhite(@ApiIgnore RequestContext rc,
                             @RequestParam AuthOptionEnum status) {
        return smsOrIpAuthService.count(status);
    }

    @ApiOperation(value = "黑白名单list")
    @BaseInfo(needAuth = AuthType.REQUIRED)
    @GetMapping(value = "/blackorwhite/list")
    public PageHelp blackorwhite(@ApiIgnore RequestContext rc,
                                 @RequestParam AuthOptionEnum key,
                                 @RequestParam(required = false, defaultValue = "1") long start,
                                 @RequestParam(required = false, defaultValue = "100") long end) {
        return smsOrIpAuthService.findAll(key, start, end);
    }

}
