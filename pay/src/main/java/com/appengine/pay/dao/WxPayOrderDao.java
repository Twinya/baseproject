package com.appengine.pay.dao;

import com.appengine.pay.domain.po.WxPayOrder;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface WxPayOrderDao extends CrudRepository<WxPayOrder, Long>, JpaSpecificationExecutor<WxPayOrder> {
    WxPayOrder findFirstByOrderNo(String orderNo);

    WxPayOrder findByOutTradeNo(String outTradeNo);

}
