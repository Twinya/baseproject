package com.appengine.task.service;

import com.appengine.sms.domain.SMSrecord;
import com.appengine.sms.service.SMSService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @Author fuyou
 * @Date 2018/12/28 14:01
 */
@Service
public class AlarmService {
    @Resource
    SMSService smsService;

    public double getSmsSuccessRate() {
        List<SMSrecord> smSrecords = smsService.getTop100();
        if (smSrecords == null || smSrecords.size() == 0) {
            return 0;
        }
        long success = smSrecords.stream().filter(Objects::nonNull).filter(a -> "success".equalsIgnoreCase(a.getMsg())).count();
        return (double) success / smSrecords.size();
    }

}
