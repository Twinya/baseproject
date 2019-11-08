package com.appengine.sms.sender;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.appengine.common.exception.EngineExceptionHelper;
import com.appengine.sms.domain.AliSMSResult;
import com.appengine.sms.domain.SMSResult;
import com.appengine.sms.domain.SMSSign;
import com.appengine.sms.exception.SMSExceptionFactor;
import org.springframework.stereotype.Component;

@Component("ali")
public class AliSender extends Sender {
    /**
     * 产品域名,开发者无需替换
     */
    public static final String ISP = "ali";
    private static final String ACCESSKEY = "LTAIF7frBbt4yrZq";
    private static final String SECRET = "cQheet7UpTAgxdcTLDcb2SCpH92zoE";
    private DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESSKEY, SECRET);
    private IAcsClient client = new DefaultAcsClient(profile);


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
        String json;
        try {
            CommonRequest request = getRequest(phoneNumber, code, sign);
            CommonResponse response = client.getCommonResponse(request);
            json = response.getData();
        } catch (ServerException e) {
            throw EngineExceptionHelper.localException(SMSExceptionFactor.FAILED);
        } catch (ClientException e) {
            throw EngineExceptionHelper.localException(SMSExceptionFactor.FAILED);
        }
        AliSMSResult result = JSON.parseObject(json, AliSMSResult.class);
        return result.toSmsResult();
    }

    private CommonRequest getRequest(String phoneNumber, String code, SMSSign sign) {
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phoneNumber);
        request.putQueryParameter("SignName", sign.getName());
        request.putQueryParameter("TemplateCode", sign.getTemplateId());
        request.putQueryParameter("TemplateParam", "{\"code\":" + code + "}");
        return request;
    }

}
