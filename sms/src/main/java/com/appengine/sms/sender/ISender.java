package com.appengine.sms.sender;

import com.appengine.sms.domain.SMSResult;
import com.appengine.sms.domain.SMSSign;

public interface ISender {
    String getIsp();

    SMSResult send(String phoneNumber, String code, SMSSign sign);

    SMSResult alarm(String phoneNumber, String content);
}
