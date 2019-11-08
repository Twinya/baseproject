package com.appengine.sms.domain;

import lombok.Data;

@Data
public class AliSMSResult {

    /**
     * Message : OK
     * RequestId : 873043ac-bcda-44db-9052-2e204c6ed20f
     * BizId : 607300000000000000^0
     * Code : OK
     */

    private String message;
    private String requestId;
    private String bizId;
    private String code;

    public SMSResult toSmsResult() {
        SMSResult result = new SMSResult();
        if ("OK".equalsIgnoreCase(code)) {
            result.setCode("0");
        } else {
            result.setCode(code);
        }
        result.setSmUuid(requestId);
        result.setMsg(message);
        return result;
    }
}
