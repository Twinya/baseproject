package com.appengine.pay.domain.po;

import com.appengine.common.exception.EngineExceptionHelper;
import com.appengine.pay.domain.PayOrder;
import com.appengine.pay.exception.PayExceptionFactor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nonnull;
import javax.persistence.*;

@Setter
@Getter
@Entity
@NoArgsConstructor
@ToString
@Table(name = "wx_pay_order", indexes = {@Index(name = "createTime", columnList = "create_time")},
        uniqueConstraints = {@UniqueConstraint(name = "order_no", columnNames = {"order_no"}), @UniqueConstraint(name = "out_trade_no", columnNames = {"out_trade_no"})})
public class WxPayOrder extends PayOrder {

    private static final long serialVersionUID = 3814548048943698702L;
    /**
     * 商品描述
     */
    @Column(nullable = false, length = 128)
    private String body;
    /**
     * 商品详情
     */
    @Column(nullable = false)
    private String detail;

    /**
     * 总金额(分)
     */
    @Column(name = "total_fee", nullable = false)
    private Integer totalFee;

    /**
     * 交易类型
     */
    @Column(name = "trade_type", nullable = false, length = 16)
    private String tradeType;

    /**
     * 返回状态码
     * SUCCESS
     */
    @Column(name = "error_code", length = 16)
    private String errorCode;

    /**
     * 返回信息
     * 处理结果的描述，信息来自于code返回结果的描述
     */
    @Column(name = "msg", length = 128)
    private String msg;

    @Override
    public Integer convertTradeStatus(@Nonnull String status) {
        if ("SUCCESS".equalsIgnoreCase(status)) {
            return 2;
        }
        if ("FAIL".equalsIgnoreCase(status)) {
            return 1;
        }
        throw EngineExceptionHelper.localException(PayExceptionFactor.PAY_FAILED);
    }
}