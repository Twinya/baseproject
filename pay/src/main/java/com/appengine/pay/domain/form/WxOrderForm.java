package com.appengine.pay.domain.form;

import com.appengine.common.entity.form.BaseForm;
import com.appengine.pay.domain.po.WxPayOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
public class WxOrderForm extends BaseForm<WxPayOrder> {
    @NotBlank
    @ApiModelProperty(value = "商品描述", example = "会员充值")
    @Size(max = 128)
    private String body;

    @NotBlank
    @ApiModelProperty(value = "商品详情", example = "腾讯充值中心-QQ会员充值")
    @Size(max = 128)
    private String detail;

    @NotBlank
    @Size(min = 8, max = 32)
    @ApiModelProperty(value = "订单号", example = "20150806125346")
    private String orderNo;

    @ApiModelProperty(value = "总金额(分)", example = "888")
    private Integer totalFee;

    @Size(max = 64)
    @ApiModelProperty(value = "终端IP", example = "123.12.12.123")
    private String spbillCreateIp;

    @NotBlank
    @Size(max = 16)
    @ApiModelProperty(value = "交易类型", example = "APP")
    private String tradeType;


}
