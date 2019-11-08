package com.appengine.pay.domain;

import com.appengine.common.entity.po.BasePo;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@Getter
@Setter
@MappedSuperclass
public abstract class PayOrder extends BasePo {
    private static final long serialVersionUID = -5924734055734681438L;

    @Column(name = "order_no", nullable = false, length = 64)
    private String orderNo;
    /**
     * 商户订单号
     */
    @Column(name = "out_trade_no", nullable = false, length = 32)
    private String outTradeNo;

    /**
     * 订单状态
     * 0:交易创建，等待买家付款
     * 1:未付款交易超时关闭，或支付完成后全额退款
     * 2:交易支付成功
     * 3:交易结束，不可退款
     */
    @Column(name = "status", nullable = false, columnDefinition = "integer default 0 COMMENT '0待付款 1订单超时或全额退款 2支付成功 3交易结束 '")
    private Integer status = 0;
    /**
     * 终端IP (调用微信支付API的机器IP)
     */
    @Column(name = "spbill_create_ip", nullable = false, length = 64)
    private String spbillCreateIp;

    /**
     * 支付渠道 wx 支付宝
     */
    @Transient
    private String payfrom;

    public abstract Integer convertTradeStatus(@Nonnull String status);
}
