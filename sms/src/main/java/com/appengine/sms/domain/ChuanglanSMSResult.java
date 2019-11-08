package com.appengine.sms.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ChuanglanSMSResult {

    /**
     * code : 117
     * msgId :
     * time : 20191030164941
     * errorMsg : 客户端IP错误
     */

    private String code;
    private String msgId;
    private String time;
    private String errorMsg;

    public SMSResult toSmsResult() {
        SMSResult result = new SMSResult();
        result.setCode(code);
        result.setSmUuid(msgId);
        result.setMsg(errorMsg);
        return result;
    }
}
