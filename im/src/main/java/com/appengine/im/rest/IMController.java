package com.appengine.im.rest;

import cn.jmessage.api.user.UserListResult;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.appengine.auth.annotation.*;
import com.appengine.auth.redis.TokenLimitRedisDao;
import com.appengine.auth.spi.MAuthSpi;
import com.appengine.common.utils.PageHelp;
import com.appengine.frame.context.RequestContext;
import com.appengine.im.domain.form.AccountForm;
import com.appengine.im.domain.po.User;
import com.appengine.im.service.IMService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Authors: fuyou
 * Version: 1.0  Created at 2015-10-02 22:08.
 */
@RestController
@Api(tags = "IM控制器")
@Validated
@RequestMapping("/api/im")
public class IMController {
    @Resource
    private TokenLimitRedisDao redisDao;
    @Autowired
    private IMService imService;

    @ApiOperation(value = "测试用", notes = "需要权限调用 成功获取客户端ip")
    @BaseInfo(desc = "test", needAuth = AuthType.REQUIRED)
    @GetMapping(value = "/getip")
    public String list(@ApiIgnore RequestContext rc) {
        return "ip:" + rc.getIp();
    }

    @ApiOperation(value = "注册im用户", notes = "频率限制：ip每分钟5次，每天500次")
    @BaseInfo(needAuth = AuthType.REQUIRED)
    @PostMapping(value = "/register", produces = "application/json")
    @RateLimit({
            @RateLimitTypeConfig(value = RateLimitType.IP, rates = {
                    @RateLimitRateConfig(value = 5, time = TimeUnit.MINUTES),
                    @RateLimitRateConfig(value = 500, time = TimeUnit.DAYS)}),})
    public JSONObject register(@ApiIgnore RequestContext rc,
                               @Valid @RequestBody AccountForm form) {
        User user = form.toPo(User.class);
        user.setIp(rc.getIp());
        return getMauthResult(imService.registerUsers(user));
    }

    @ApiOperation(value = "im用户登陆", notes = "频率限制：ip每分钟5次，每天500次")
    @BaseInfo(needAuth = AuthType.REQUIRED)
    @PostMapping(value = "/login", produces = "application/json")
    @RateLimit({
            @RateLimitTypeConfig(value = RateLimitType.IP, rates = {
                    @RateLimitRateConfig(value = 5, time = TimeUnit.MINUTES),
                    @RateLimitRateConfig(value = 500, time = TimeUnit.DAYS)}),})
    public JSONObject login(@ApiIgnore RequestContext rc,
                            @Valid @RequestBody AccountForm form) {
        return getMauthResult(imService.login(form.getUsername(), null, form.getPassword()));
    }

    private JSONObject getMauthResult(User user) {
        JSONObject result = (JSONObject) JSON.toJSON(user);
        long time = System.currentTimeMillis();
        String mauth = MAuthSpi.generateMauth(time, user.getId());
        result.put("mauth", mauth);
        redisDao.setToken(user.getId(), time + "");
        return result;
    }

    @ApiOperation(value = "添加黑名单", notes = "频率限制：ip每分钟5次，每天500次")
    @BaseInfo(needAuth = AuthType.REQUIRED)
    @PostMapping(value = "/blacklist/{owner_username}", produces = "application/json")
    @RateLimit({
            @RateLimitTypeConfig(value = RateLimitType.IP, rates = {
                    @RateLimitRateConfig(value = 5, time = TimeUnit.MINUTES),
                    @RateLimitRateConfig(value = 500, time = TimeUnit.DAYS)}),})
    public void addBlack(@ApiIgnore RequestContext rc,
                         @PathVariable String owner_username,
                         @Valid @RequestParam String[] black_usernames) {
        imService.addBlackList(owner_username, black_usernames);
    }

    @ApiOperation(value = "移除黑名单")
    @BaseInfo(needAuth = AuthType.REQUIRED)
    @DeleteMapping(value = "/blacklist/{owner_username}")
    public Integer remove(@ApiIgnore RequestContext rc,
                          @PathVariable String owner_username,
                          @Valid @RequestParam List<String> black_usernames) {
        return imService.RemoveBlacklist(owner_username, black_usernames);
    }

    @ApiOperation(value = "黑名单列表")
    @BaseInfo(needAuth = AuthType.REQUIRED)
    @GetMapping(value = "/blacklist/{username}")
    public PageHelp<String> list(@ApiIgnore RequestContext rc,
                                 @RequestParam(required = false, defaultValue = "1") long page,
                                 @RequestParam(required = false, defaultValue = "100") long pageSize,
                                 @PathVariable String username) {
        return imService.getBlackList(username, page, pageSize);
    }

    @ApiOperation(value = "用户列表")
    @BaseInfo(needAuth = AuthType.REQUIRED)
    @GetMapping("/users")
    public UserListResult getUserList() {
        return imService.getUserList();
    }

}



