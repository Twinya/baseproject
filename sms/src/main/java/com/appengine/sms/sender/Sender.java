package com.appengine.sms.sender;

import com.appengine.common.exception.EngineExceptionHelper;
import com.appengine.sms.domain.SMSResult;
import com.appengine.sms.domain.SMSSign;
import com.appengine.sms.exception.SMSExceptionFactor;
import com.appengine.sms.utils.PhoneNumber;

/**
 * @Author fuyou
 * @Date 2019/2/13 13:40
 */
public abstract class Sender implements ISender {

    abstract SMSResult sendBySign(String phoneNumber, String code, SMSSign sign);

    @Override
    public SMSResult send(String phoneNumber, String code, SMSSign sign) {
        if (sign == null) {
            throw EngineExceptionHelper.localException(SMSExceptionFactor.NO_SIGN);
        }
        checkPhoneNumber(phoneNumber);
        return sendBySign(phoneNumber, code, sign);
    }

    void checkPhoneNumber(String phoneNumber) {
        if (!PhoneNumber.getInstance().verify(phoneNumber)) {
            throw EngineExceptionHelper.localException(SMSExceptionFactor.WRONG_NUMBER);
        }
    }
}
