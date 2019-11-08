package com.appengine.user.rest;

import com.appengine.auth.annotation.*;
import com.appengine.auth.redis.VerifyCodeRedisDao;
import com.appengine.common.exception.EngineExceptionHelper;
import com.appengine.frame.context.RequestContext;
import com.appengine.sms.domain.SMSResult;
import com.appengine.sms.service.SMSService;
import com.appengine.sms.utils.VerifyUtils;
import com.appengine.user.domain.form.SMSForm;
import com.appengine.user.service.SmsOrIpAuthService;
import com.appengine.user.service.UserService;
import com.appengine.user.utils.UserExcepFactor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Authors: fuyou
 * Version: 1.0  Created at 2015-10-02 22:08.
 */
@Api(tags = "短信")
@Validated
@RestController
@RequestMapping("/api/SMS")
public class SMSController {

    @Resource
    private SMSService smsServer;
    @Resource
    private UserService userService;
    @Resource
    private VerifyCodeRedisDao verifyCodeRedisDao;
    @Resource
    private SmsOrIpAuthService smsOrIpAuthService;

    @BaseInfo(needAuth = AuthType.OPTION)
    @ApiOperation(value = "发送短信验证码")
    @PostMapping(value = "/send")
    @RateLimit({
            @RateLimitTypeConfig(value = RateLimitType.IP, rates = {
                    @RateLimitRateConfig(value = 2, time = TimeUnit.MINUTES),
                    @RateLimitRateConfig(value = 100, time = TimeUnit.DAYS)})})
    public SMSResult send(@ApiIgnore RequestContext rc, @Valid @RequestBody SMSForm form) {

        boolean u = userService.containsUser(form.getMobile(), form.getFrom());
        if (form.getType() == 0 && u) {
            throw EngineExceptionHelper.localException(UserExcepFactor.ACCOUNT_EXISTS);
        } else if (form.getType() == 1 || form.getType() == 0) {
            return smsServer.sendBySign(form.getType(), form.getMobile(), form.getSign(), rc.getIp());
        } else {
            throw EngineExceptionHelper.localException(UserExcepFactor.TYPE_ERROR);
        }
//        if (verifyCodeRedisDao.isValidCode(requestId, code)) {
//        return smsServer.sendBySign(mobile, sign, type);
//        } else {
//            throw EngineExceptionHelper.localException(SMSExceptionFactor.VERIFY_FAILED);
//        }
    }
//
//    @BaseInfo(desc = "发送短信验证码", needAuth = AuthType.OPTION)
//    @PostMapping(value = "/sendCode")
//    @RateLimit({
//            @RateLimitTypeConfig(value = RateLimitType.IP, rates = {@RateLimitRateConfig(value = 50, time = TimeUnit.MINUTES)})})
//    public SMSResult sendByCode(@RequestParam String mobile,
//                                @RequestParam String sign,
//                                @RequestParam String code) {
//        return smsServer.sendByCode(mobile, sign, code);
//    }

    @BaseInfo(needAuth = AuthType.OPTION)
    @ApiOperation(value = "验证短信")
    @PostMapping(value = "/valid")
    public boolean validSms(@RequestParam String mobile,
                            @RequestParam String requestId,
                            @RequestParam String code) {
        return smsServer.validCode(requestId, mobile, code);
    }

    @RateLimit({
            @RateLimitTypeConfig(value = RateLimitType.IP, rates = {
                    @RateLimitRateConfig(value = 30, time = TimeUnit.MINUTES),
                    @RateLimitRateConfig(value = 10000, time = TimeUnit.DAYS)}),
    })
    @GetMapping(value = "/image", produces = MediaType.IMAGE_JPEG_VALUE)
    @ApiOperation(value = "图片验证码")
    @BaseInfo(needAuth = AuthType.OPTION)
    @ResponseBody
    public void getImage(HttpServletResponse response,
                         @RequestParam String requestId) throws IOException {
        //设置相应类型,告诉浏览器输出的内容为图片
        response.setContentType("image/jpeg");
        //设置响应头信息，告诉浏览器不要缓存此内容
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expire", 0);
        String verifyCode = VerifyUtils.generateVerifyCode(4);
        verifyCodeRedisDao.setCode(requestId, verifyCode);
        VerifyUtils.outputImage(100, 40, response.getOutputStream(), verifyCode);
    }

}
