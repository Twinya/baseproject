package com.appengine.pay.utils;

import com.appengine.pay.domain.PayOrder;

public interface PayNotifyListener {

    public void onSuccess(PayOrder payOrder);

    public void onFailed();

}
