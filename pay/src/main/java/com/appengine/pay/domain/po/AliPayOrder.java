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
import java.math.BigDecimal;

@Setter
@Getter
@Entity
@NoArgsConstructor
@ToString
@Table(name = "ali_pay_order", indexes = {@Index(name = "createTime", columnList = "create_time")},
        uniqueConstraints = {@UniqueConstraint(name = "order_no", columnNames = {"order_no"}), @UniqueConstraint(name = "out_trade_no", columnNames = {"out_trade_no"})})
public class AliPayOrder extends PayOrder {

    private static final long serialVersionUID = 7954582935258728574L;
    /**
     * 商品的标题/交易标题/订单标题/订单关键字等。
     */
    @Column(nullable = false)
    private String subject;
    /**
     * 对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body。
     */
    @Column(nullable = false, length = 128)
    private String body;
    /**
     * 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
     */
    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Override
    public Integer convertTradeStatus(@Nonnull String trade_status) {
        if ("WAIT_BUYER_PAY".equalsIgnoreCase(trade_status)) {
            return 0;
        }
        if ("TRADE_CLOSED".equalsIgnoreCase(trade_status)) {
            return 1;
        }
        if ("TRADE_SUCCESS".equalsIgnoreCase(trade_status)) {
            return 2;
        }
        if ("TRADE_FINISHED".equalsIgnoreCase(trade_status)) {
            return 3;
        }
        throw EngineExceptionHelper.localException(PayExceptionFactor.PAY_FAILED);
    }
}