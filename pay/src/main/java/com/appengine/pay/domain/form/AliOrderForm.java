package com.appengine.pay.domain.form;

import com.appengine.common.entity.form.BaseForm;
import com.appengine.pay.domain.po.AliPayOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
public class AliOrderForm extends BaseForm<AliPayOrder> {
    @NotBlank
    @ApiModelProperty(value = "商品描述", example = "腾讯充值")
    @Size(max = 128)
    private String subject;

    @NotBlank
    @ApiModelProperty(value = "商品详情", example = "腾讯充值中心-QQ会员充值")
    @Size(max = 128)
    private String body;

    @NotBlank
    @Size(min = 8, max = 32)
    @ApiModelProperty(value = "订单号", example = "20150806125346")
    private String orderNo;

    @NotNull
    @ApiModelProperty(value = "总金额(单位为元，精确到小数点后两位)", example = "8.88")
    private BigDecimal totalAmount;

    @Size(max = 64)
    @ApiModelProperty(value = "终端IP", example = "123.12.12.123")
    private String spbillCreateIp;
}
