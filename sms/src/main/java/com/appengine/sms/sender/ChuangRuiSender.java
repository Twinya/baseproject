package com.appengine.sms.sender;

import com.alibaba.fastjson.JSON;
import com.appengine.common.exception.EngineExceptionHelper;
import com.appengine.frame.utils.HttpUtil;
import com.appengine.sms.domain.SMSResult;
import com.appengine.sms.domain.SMSSign;
import com.appengine.sms.exception.SMSExceptionFactor;
import com.squareup.okhttp.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.LinkedHashMap;

@Component("chuangrui")
public class ChuangRuiSender extends Sender {
    /**
     * 产品域名,开发者无需替换
     */
    public static final String ISP = "chuangrui";
    public static final String API_SERVER = "https://api.1cloudsp.com/api/v2/single_send";
    private static final String ACCESSKEY = "u58386PdVtwdgRKE";
    private static final String SECRET = "iQY2CHekebSLKqyirjzj8sDfZdmIITO8";

    @Override
    public String getIsp() {
        return ISP;
    }

    @Override
    public SMSResult sendBySign(String phoneNumber, String code, SMSSign sign) {
        Response response = HttpUtil.get(API_SERVER, getParams(phoneNumber, code, sign.getName(), sign.getTemplateId()));
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
        SMSResult result = JSON.parseObject(json, SMSResult.class);
        return result;
    }

    private LinkedHashMap<String, String> getParams(String phoneNumber, String code, String sign, String templateId) {
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("accesskey", ACCESSKEY);
        params.put("secret", SECRET);
        params.put("sign", sign);
        params.put("templateId", templateId);
        params.put("mobile", phoneNumber);
        params.put("content", code);
        return params;
    }

    @Override
    public SMSResult alarm(String phoneNumber, String content) {
        checkPhoneNumber(phoneNumber);
        Response response = HttpUtil.get(API_SERVER, getParams(phoneNumber, content, "【简零科技】", "33183"));
        return getSmsResult(response);
    }
}
