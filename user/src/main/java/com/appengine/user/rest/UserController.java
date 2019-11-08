package com.appengine.user.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.appengine.auth.annotation.*;
import com.appengine.auth.redis.TokenLimitRedisDao;
import com.appengine.auth.spi.MAuthSpi;
import com.appengine.frame.context.RequestContext;
import com.appengine.frame.utils.StatisticsTimeUtils;
import com.appengine.user.domain.form.LoginForm;
import com.appengine.user.domain.form.ResetPasswordForm;
import com.appengine.user.domain.form.UserForm;
import com.appengine.user.domain.po.LoginNumByDay;
import com.appengine.user.domain.po.LoginRecord;
import com.appengine.user.domain.po.User;
import com.appengine.user.service.LoginRecordService;
import com.appengine.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Authors: fuyou
 * Version: 1.0  Created at 2015-10-02 22:08.
 */
@Api("账号")
@Validated
@RestController
@RequestMapping("/api/account")
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private LoginRecordService loginRecordService;
    @Resource
    private TokenLimitRedisDao redisDao;

    @ApiOperation(value = "注册用户", notes = "频率限制：ip每分钟5次，每天500次")
    @BaseInfo(desc = "注册用户", needAuth = AuthType.OPTION)
    @PostMapping(value = "/register", produces = "application/json")
    @RateLimit({
            @RateLimitTypeConfig(value = RateLimitType.IP, rates = {
                    @RateLimitRateConfig(value = 5, time = TimeUnit.MINUTES),
                    @RateLimitRateConfig(value = 500, time = TimeUnit.DAYS)}),})
    public JSONObject register(@ApiIgnore RequestContext rc,
                               @Valid @RequestBody UserForm form) {
        User user = form.toPo(User.class);
        user.setIp(rc.getIp());
        userService.register(user, form.getRegistFrom(), form.getCode(), form.getRequestId(), form.getPhone_os());
        return getMauthResult(user);
    }

    private JSONObject getMauthResult(User user) {
        JSONObject result = (JSONObject) JSON.toJSON(user);
        long time = System.currentTimeMillis();
        String mauth = MAuthSpi.generateMauth(time, user.getId());
        result.put("mauth", mauth);
        redisDao.setToken(user.getId(), time + "");
        return result;
    }

    @ApiOperation(value = "刷新token", notes = "频率限制：ip每分钟5次")
    @BaseInfo(desc = "刷新token", needAuth = AuthType.REQUIRED)
    @PostMapping(value = "/refreshToken", produces = "application/json")
    @RateLimit({
            @RateLimitTypeConfig(value = RateLimitType.IP, rates = {
                    @RateLimitRateConfig(value = 5, time = TimeUnit.MINUTES)}),
    })
    public JSONObject refreshToken(@ApiIgnore RequestContext rc) {
        User user = userService.get(rc.getCurrentUid());
        return getMauthResult(user);
    }

    @ApiOperation(value = "登陆", notes = "频率限制：ip每分钟5次，每天500次")
    @BaseInfo(desc = "登陆", needAuth = AuthType.OPTION)
    @PostMapping(value = "/login", produces = "application/json")
    @RateLimit({
            @RateLimitTypeConfig(value = RateLimitType.IP, rates = {
                    @RateLimitRateConfig(value = 5, time = TimeUnit.MINUTES),
                    @RateLimitRateConfig(value = 500, time = TimeUnit.DAYS)}),
    })
    public JSONObject login(@ApiIgnore RequestContext rc,
                            @Valid @RequestBody LoginForm loginForm) {
        User user = userService.login(loginForm.getUsername(), loginForm.getRegistFrom(), loginForm.getPassword());
        LoginRecord record = getLoginRecord(user.getChannel(), rc.getIp(),
                loginForm.getAddress().getProvince(), loginForm.getAddress().getCity(),
                loginForm.getAddress().getDistrict(), loginForm.getAddress().getStreet(),
                loginForm.getAddress().getStreetNum(), loginForm.getBrand(), loginForm.getPhone_os(), user, null, null, loginForm.getApp());
        record.setCreateTime(System.currentTimeMillis());
        loginRecordService.add(record);
        return getMauthResult(user);
    }

    @ApiOperation(value = "验证码登陆", notes = "频率限制：ip每分钟5次，每天500次")
    @BaseInfo(desc = "登陆", needAuth = AuthType.OPTION)
    @PostMapping(value = "/login/code", produces = "application/json")
    @RateLimit({
            @RateLimitTypeConfig(value = RateLimitType.IP, rates = {
                    @RateLimitRateConfig(value = 5, time = TimeUnit.MINUTES),
                    @RateLimitRateConfig(value = 500, time = TimeUnit.DAYS)}),
    })
    public JSONObject loginCode(
            @ApiIgnore RequestContext rc,
            @Valid @RequestBody LoginForm loginForm) {
        User user = userService.login(loginForm.getUsername(), loginForm.getRegistFrom(), loginForm.getCode(), loginForm.getRequestId(), loginForm.getChannel(), rc.getIp(), loginForm.getPhone_os());
        LoginRecord record = getLoginRecord(user.getChannel(), rc.getIp(),
                loginForm.getAddress().getProvince(), loginForm.getAddress().getCity(),
                loginForm.getAddress().getDistrict(), loginForm.getAddress().getStreet(),
                loginForm.getAddress().getStreetNum(), loginForm.getBrand(), loginForm.getPhone_os(), user, null, null, loginForm.getApp());
        record.setCreateTime(System.currentTimeMillis());
        loginRecordService.add(record);
        return getMauthResult(user);
    }

    @ApiOperation(value = "退出登陆")
    @BaseInfo(desc = "退出登陆", needAuth = AuthType.REQUIRED)
    @PostMapping(value = "/logout", produces = "application/json")
    public String logout(@ApiIgnore RequestContext rc) {
        redisDao.removeToken(rc.getCurrentUid());
        return "success";
    }

    @ApiOperation(value = "修改密码")
    @BaseInfo(desc = "修改密码", needAuth = AuthType.REQUIRED)
    @PostMapping(value = "/changepassword", produces = "application/json")
    public User changePassword(@ApiIgnore RequestContext rc,
                               @RequestParam String password,
                               @RequestParam String newPassword) {
        return userService.changePassword(rc.getCurrentUid(), password, newPassword);
    }

    @ApiOperation(value = "重置密码")
    @BaseInfo(desc = "重置密码", needAuth = AuthType.OPTION)
    @PostMapping(value = "/resetpassword", produces = "application/json")
    public User resetPassword(@Valid @RequestBody ResetPasswordForm form) {
        return userService.resetPassword(form.getUsername(), form.getRegistFrom(), form.getPassword(), form.getCode(), form.getRequestId());
    }

    @ApiOperation(value = "用户信息")
    @GetMapping(value = "/profile", produces = "application/json")
    @BaseInfo(status = ApiStatus.PUBLIC, needAuth = AuthType.REQUIRED)
    public User show(@ApiIgnore RequestContext rc) {
        long uid = rc.getCurrentUid();
        return userService.get(uid);
    }

    @ApiOperation(value = "设置用户信息")
    @PostMapping(value = "/profile", produces = "application/json")
    @BaseInfo(desc = "用户信息", status = ApiStatus.PUBLIC, needAuth = AuthType.REQUIRED)
    public User set(@ApiIgnore RequestContext rc,
                    @RequestParam(required = false) String realName,
                    @RequestParam(required = false) String portrait,
                    @RequestParam(required = false) String mobile,
                    @RequestParam(required = false) String gender) {
        Long uid = rc.getCurrentUid();
        User u = userService.get(uid);
        u.setUpdatedTime(Date.from(ZonedDateTime.now().toInstant()));
        if (realName != null) {
            u.setRealName(realName);
        }
        if (mobile != null) {
            u.setMobile(mobile);
        }
        if (portrait != null) {
            u.setHeadurl(portrait);
        }
        if (gender != null) {
            u.setSex(gender);
        }
        return userService.set(u);
    }

    @BaseInfo(needAuth = AuthType.REQUIRED)
    @GetMapping(value = "/channels/save", produces = "application/json")
    public Iterable<LoginNumByDay> saveAllChannel2MysqlByDay() {
        return loginRecordService.saveAllChannel2MysqlByDay();
    }


    /**
     * 组装登陆日志
     */
    private LoginRecord getLoginRecord(String channel, String ip, String province, String city, String district,
                                       String street, String streetNum, String brand, String phone_os,
                                       User user, String code, String requestId, String app) {
        LoginRecord loginRecord = loginRecordService.getByUid(user.getId());
        LoginRecord record = new LoginRecord();
        if (loginRecord == null && StatisticsTimeUtils.getToday() == StatisticsTimeUtils.getDateFromLong(user.getCreatedTime().getTime())) {
            record.setFirstLogn(true);
        } else {
            record.setFirstLogn(false);
        }
        record.setChannel(channel);
        record.setIp(ip);
        record.setUid(user.getId());
        record.setProvince(province);
        record.setCity(city);
        record.setDistrict(district);
        record.setStreet(street);
        record.setStreetNum(streetNum);
        record.setBrand(brand);
        record.setPhone_os(phone_os);
        record.setCode(code);
        record.setRequestId(requestId);
        record.setApp(app);
        return record;
    }

}
