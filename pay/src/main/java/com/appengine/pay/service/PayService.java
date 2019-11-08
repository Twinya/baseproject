package com.appengine.pay.service;

import com.appengine.pay.utils.PayConfig;
import com.appengine.pay.utils.PayNotifyListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class PayService {
    PayConfig payConfig;
    PayNotifyListener payNotifyListener;

    public PayNotifyListener getPayNotifyListener() {
        return payNotifyListener;
    }

    @Autowired(required = false)
    public void setPayNotifyListener(PayNotifyListener payNotifyListener) {
        this.payNotifyListener = payNotifyListener;
    }
}
