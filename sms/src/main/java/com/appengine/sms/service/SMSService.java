package com.appengine.sms.service;

import com.appengine.auth.redis.SMSRedisDao;
import com.appengine.common.exception.EngineExceptionHelper;
import com.appengine.sms.dao.SMSDao;
import com.appengine.sms.dao.SMSSignDao;
import com.appengine.sms.domain.SMSResult;
import com.appengine.sms.domain.SMSSign;
import com.appengine.sms.domain.SMSrecord;
import com.appengine.sms.exception.SMSExceptionFactor;
import com.appengine.sms.sender.ISender;
import com.appengine.sms.utils.RandomCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SMSService implements ApplicationContextAware, InitializingBean {

    @Resource
    SMSSignDao smsSignDao;

    @Resource
    private SMSDao smsDao;

    @Resource
    SMSRedisDao redisDao;

    private ApplicationContext context;
    private Map<String, ISender> senderMap = new HashMap<>();
    private List<ISender> senders = new ArrayList<>();

    /**
     * 产品域名,开发者无需替换
     */
    private static final String SUCCESS_SMS_RESULT = "0";
    public static volatile Map<String, SMSSign> signs;

    public SMSResult sendBySign(int type, String phoneNumber, String sign, String ip) {
        initSign();
        String code = redisDao.contains(phoneNumber);
        return getSmsResult(type, phoneNumber, signs.get(sign), code, ip);
    }

    @NotNull
    private SMSResult getSmsResult(int type, String phoneNumber, SMSSign sign, String code, String ip) {
        if (sign == null) {
            throw EngineExceptionHelper.localException(SMSExceptionFactor.NO_SIGN);
        }
        if (code == null) {
            code = RandomCode.getCode();
        }
        SMSResult result = getSender(sign.getIsp()).send(phoneNumber, code, sign);
        SMSrecord smSrecord = new SMSrecord();
        smSrecord.setPhoneNumber(phoneNumber);
        smSrecord.setCode(code);
        smSrecord.setIp(ip);
        smSrecord.setType(type);
        smSrecord.setMsg(result.getMsg());
        if (result.getSmUuid() == null) {
            smSrecord.setRequestId(result.getCode());
        } else {
            smSrecord.setRequestId(result.getSmUuid());
        }
        savesms(smSrecord);
        if (SUCCESS_SMS_RESULT.equals(result.getCode())) {
            redisDao.setSMS(phoneNumber, result.getSmUuid(), code);
        } else {
            throw EngineExceptionHelper.localException(SMSExceptionFactor.FAILED, result.getMsg());
        }
        return result;
    }

    protected void initSign() {
        initMap();
        if (signs.size() == 0) {
            refresh();
        }
    }

    protected synchronized void initMap() {
        if (signs == null) {
            signs = new ConcurrentHashMap<>(8);
        }
    }

    public Iterable<SMSSign> refresh() {
        if (signs == null) {
            initMap();
        }
        Iterable<SMSSign> smsSigns = smsSignDao.findAll();
        smsSigns.forEach(sign -> signs.put(sign.getSign(), sign));
        return smsSigns;
    }

    public SMSSign addSign(String sign, String name, String templateId) {
        SMSSign smsSign = smsSignDao.findBySign(sign);
        if (smsSign != null) {
            throw EngineExceptionHelper.localException(SMSExceptionFactor.SIGN_EXIST);
        } else {
            smsSign = smsSignDao.save(new SMSSign(sign, name, templateId));
        }
        refresh();
        return smsSign;
    }

    public SMSSign editSign(long id, String sign, String name, String templateId) {
        SMSSign smsSign = smsSignDao.findBySign(sign);
        if (smsSign != null && (smsSign.getId() == id)) {
            throw EngineExceptionHelper.localException(SMSExceptionFactor.SIGN_EXIST);
        }
        smsSign = smsSignDao.findById(id).orElseThrow(() -> EngineExceptionHelper.localException(SMSExceptionFactor.NO_SIGN));
        smsSign.setSign(sign);
        smsSign.setName(name);
        smsSign.setTemplateId(templateId);
        smsSignDao.save(smsSign);
        refresh();
        return smsSign;
    }

    public List<SMSrecord> getTop100() {
        return smsDao.findTop100ByOrderByIdDesc();
    }

    public boolean validCode(String requestId, String mobile, String code) {
        return redisDao.isValidCode(requestId, mobile, code);
    }

    public void savesms(SMSrecord entity) {
        smsDao.save(entity);
    }


    public ISender getSender(String isp) {
        if (senderMap.containsKey(isp)) {
            return this.senderMap.get(isp.toLowerCase());
        } else {
            throw EngineExceptionHelper.localException(SMSExceptionFactor.NO_ISP);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, ISender> spis = this.context.getBeansOfType(ISender.class);

        for (ISender sender : spis.values()) {
            senders.add(sender);
            senderMap.put(StringUtils.lowerCase(sender.getIsp()), sender);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
