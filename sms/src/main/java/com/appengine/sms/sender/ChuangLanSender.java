package com.appengine.sms.sender;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.appengine.common.exception.EngineExceptionHelper;
import com.appengine.frame.utils.HttpUtil;
import com.appengine.sms.domain.ChuanglanSMSResult;
import com.appengine.sms.domain.SMSResult;
import com.appengine.sms.domain.SMSSign;
import com.appengine.sms.exception.SMSExceptionFactor;
import com.squareup.okhttp.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.LinkedHashMap;

@Component("chuanglan")
public class ChuangLanSender extends Sender {
    /**
     * 产品域名,开发者无需替换
     */
    public static final String ISP = "chuanglan";
    private static final String API_SERVER = "http://smssh1.253.com/msg/send/json";
    private static final String ACCESSKEY = "N5741542";
    private static final String SECRET = "ZWT3qQvCGn85fd";

    @Override
    public String getIsp() {
        return ISP;
    }

    @Override
    public SMSResult alarm(String phoneNumber, String content) {
        return null;
    }

    @Override
    public SMSResult sendBySign(String phoneNumber, String code, SMSSign sign) {
        Response response = HttpUtil.doPost(API_SERVER, getParams(phoneNumber, code, sign.getName()));
        return getSmsResult(response);
    }

    private SMSResult getSmsResult(Response response) {
        if (response == null) {
            throw EngineExceptionHelper.localException(SMSExceptionFactor.FAILED);
        }
        String json;
        try {
            json = response.body().string();
        } catch (IOException e) {
            throw EngineExceptionHelper.localException(SMSExceptionFactor.FAILED);
        }
        ChuanglanSMSResult result = JSON.parseObject(json, ChuanglanSMSResult.class);
        return result.toSmsResult();
    }

    private String getParams(String phoneNumber, String code, String sign) {
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("account", ACCESSKEY);//API账号
        params.put("password", SECRET);//API密码
        params.put("msg", sign + "验证码" + code + "您正在绑定手机号，此验证码5分钟内有效，如非本人操作请忽略。");//短信内容
        params.put("phone", phoneNumber);//手机号
        return JSONObject.toJSONString(params);
    }

    public static void main(String[] args) {
        Sender sender = new ChuangLanSender();
        SMSSign smsSign = new SMSSign();
        smsSign.setIsp(sender.getIsp());
        smsSign.setName("【fun】");
        smsSign.setSign("fun");
        System.out.println(sender.sendBySign("", "1234", smsSign));
    }
}
