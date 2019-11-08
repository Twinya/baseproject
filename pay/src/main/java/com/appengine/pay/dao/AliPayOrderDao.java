package com.appengine.pay.dao;

import com.appengine.pay.domain.po.AliPayOrder;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface AliPayOrderDao extends CrudRepository<AliPayOrder, Long>, JpaSpecificationExecutor<AliPayOrder> {
    AliPayOrder findFirstByOrderNo(String orderNo);

    AliPayOrder findByOutTradeNo(String outTradeNo);

}
