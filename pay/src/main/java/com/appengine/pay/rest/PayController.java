package com.appengine.pay.rest;

import com.appengine.auth.annotation.*;
import com.appengine.frame.context.RequestContext;
import com.appengine.pay.domain.form.AliOrderForm;
import com.appengine.pay.domain.form.WxOrderForm;
import com.appengine.pay.domain.po.AliPayOrder;
import com.appengine.pay.domain.po.WxPayOrder;
import com.appengine.pay.service.AliPayService;
import com.appengine.pay.service.WxPayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

/**
 * Authors: fuyou
 * Version: 1.0  Created at 2015-10-02 22:08.
 */
@RestController
@Api(tags = "支付控制器")
@Validated
@RequestMapping("/api/pay")
public class PayController {
    private final AliPayService aliPayService;
    private final WxPayService wxPayService;

    @Autowired
    public PayController(AliPayService aliPayService, WxPayService wxPayService) {
        this.aliPayService = aliPayService;
        this.wxPayService = wxPayService;
    }

    @ApiOperation(value = "支付宝支付")
    @BaseInfo(needAuth = AuthType.REQUIRED)
    @PostMapping(value = "/alipay")
    public String pay(@ApiIgnore RequestContext rc,
                      @Valid @RequestBody AliOrderForm form) {
        form.setSpbillCreateIp(rc.getIp() == null ? form.getSpbillCreateIp() : rc.getIp());
        return aliPayService.pay(form.toPo(AliPayOrder.class));
    }


    /**
     * 接口地址以 Notify 结尾将不会被转换为json格式 原样返回
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "接受支付宝通知")
    @BaseInfo(needAuth = AuthType.OPTION)
    @PostMapping(value = "/aliNotify")
    @ResponseBody
    public String aliReceive(HttpServletRequest request) {
        return aliPayService.aliNotify(request);
    }

    @ApiOperation(value = "WX支付")
    @BaseInfo(needAuth = AuthType.REQUIRED)
    @PostMapping(value = "/wx")
    public Map<String, String> Wxpay(@ApiIgnore RequestContext rc,
                                     @Valid @RequestBody WxOrderForm form) throws Exception {
        form.setSpbillCreateIp(rc.getIp() == null ? form.getSpbillCreateIp() : rc.getIp());
        WxPayOrder wxPayOrder = form.toPo(WxPayOrder.class);
        return wxPayService.pay(wxPayOrder);
    }

    @ApiOperation(value = "接受微信通知", notes = "如果参数notify_url无法访问，商户将无法接收到微信通知。")
    @BaseInfo(needAuth = AuthType.OPTION)
    @ResponseBody
    @PostMapping(value = "/wxNotify")
    public String wxReceive(HttpServletRequest request) throws Exception {
        return wxPayService.getNotify(request);
    }

    @ApiOperation(value = "订单查询", notes = "该接口提供所有微信支付订单的查询，商户可以通过该接口主动查询订单状态，完成下一步的业务逻辑")
    @BaseInfo(needAuth = AuthType.OPTION)
    @ResponseBody
    @PostMapping(value = "/wx/orderQuery")
    public Map<String, String> wxOrderquery(@RequestParam String outTradeNo) throws Exception {
        return wxPayService.getOrderInfo(outTradeNo);
    }

    @ApiOperation(value = "退款查询", notes = "提交退款申请后，通过调用该接口查询退款状态")
    @BaseInfo(needAuth = AuthType.OPTION)
    @ResponseBody
    @PostMapping(value = "/wx/refundQuery")
    public Map<String, String> wxrefundQuery(@RequestParam String transactionId) throws Exception {
        return wxPayService.getRefundInfo(transactionId);
    }


}



