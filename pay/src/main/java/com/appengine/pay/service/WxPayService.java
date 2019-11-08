package com.appengine.pay.service;

import com.appengine.common.exception.EngineExceptionHelper;
import com.appengine.pay.dao.WxPayOrderDao;
import com.appengine.pay.domain.po.WxPayOrder;
import com.appengine.pay.exception.PayExceptionFactor;
import com.appengine.pay.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.jdom2.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Service
public class WxPayService extends PayService {

    private final WxPayOrderDao wxPayOrderDao;

    @Autowired
    public WxPayService(WxPayOrderDao wxPayOrderDao, PayConfig payConfig) {
        this.wxPayOrderDao = wxPayOrderDao;
        this.payConfig = payConfig;
    }

    private static final String FAIL = XMLUtil.setXML("FAIL", "FAIL");

    private static final String SUCCESS = XMLUtil.setXML("SUCCESS", "OK");

    public Map<String, String> pay(WxPayOrder wxPayOrder) throws Exception {
        SortedMap<String, String> requestBody = getRequestBody(wxPayOrder);
        Map<String, String> resultMap = getResultMap(requestWx(payConfig.getWxGateUrl(), "POST", requestBody));
        wxPayOrder.setOutTradeNo(requestBody.get("out_trade_no"));
        saveWxPayOrder(wxPayOrder, resultMap);
        return resultMap;
    }

    private String requestWx(String constants, String method, SortedMap<String, String> requestBody) {
        return XMLUtil.httpsRequest(constants, method, XMLUtil.getRequestXml(requestBody));
    }

    private void saveWxPayOrder(WxPayOrder wxPayOrder, Map<String, String> resultMap) {
        wxPayOrder.setErrorCode(resultMap.get("return_code"));
        wxPayOrder.setMsg(resultMap.get("return_msg"));
        try {
            wxPayOrderDao.save(wxPayOrder);
        } catch (Exception e) {
            throw EngineExceptionHelper.localException(PayExceptionFactor.PAY_FAILED, "orderNo 重复, 重新生成后提交");
        }
    }

    private Map<String, String> getResultMap(String xmlStr) throws Exception {
        Map<String, String> map = XMLUtil.doXMLParse(xmlStr);
        if (map.get("return_code").equals("SUCCESS")) {
            SortedMap<String, String> result = new TreeMap<>();
            result.put("appid", payConfig.getWxAppId());
            result.put("mch_id", payConfig.getWxMchId());
            result.put("prepayid", map.get("prepay_id"));
            result.put("package", "WXPay");
            result.put("noncestr", map.get("nonce_str"));
            result.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
            String sign1 = PayUtil.createSign(result, payConfig.getWxApiKey());
            map.put("sign", sign1);
            map.put("timestamp", String.valueOf(result.get("timestamp")));
            map.put("package", "WXPay");
        } else {
            throw EngineExceptionHelper.localException(PayExceptionFactor.PAY_FAILED);
        }
        return map;
    }

    private SortedMap<String, String> getRequestBody(WxPayOrder wxPayOrder) throws Exception {
        SortedMap<String, String> requestBody = new TreeMap<>();
        requestBody.put("appid", payConfig.getWxAppId());
        requestBody.put("body", wxPayOrder.getBody());
        requestBody.put("mch_id", payConfig.getWxMchId());
        requestBody.put("nonce_str", PayUtil.generateNonceStr());
        requestBody.put("notify_url", payConfig.getDomain() + payConfig.getWxNotifyUrl());
        requestBody.put("out_trade_no", SnUtils.generateOrderNo(wxPayOrder.getOrderNo()));
        requestBody.put("total_fee", String.valueOf(wxPayOrder.getTotalFee()));
        requestBody.put("trade_type", wxPayOrder.getTradeType());
        requestBody.put("spbill_create_ip", wxPayOrder.getSpbillCreateIp());
        String sign = PayUtil.createSign(requestBody, payConfig.getWxApiKey());
        requestBody.put("sign", sign);
        return requestBody;
    }


    public String getNotify(HttpServletRequest request) throws Exception {
        InputStream inStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        String result = new String(outSteam.toByteArray(), StandardCharsets.UTF_8);
        Map<String, String> map = null;
        WxPayOrder wxPayOrder = null;
        try {
            map = XMLUtil.doXMLParse(result);
        } catch (JDOMException e) {
            e.printStackTrace();
        }
        boolean signVerified = PayUtil.isSignatureValid(map, payConfig.getWxApiKey());
        if (!signVerified) {
            return FAIL;
        } else {
            if (map.get("return_code").equals("SUCCESS")) {
                String outTradeNo = map.get("out_trade_no");
                String Price = map.get("total_fee");
                double price = Double.parseDouble(Price) / 100;
                String amount = Double.toString(price);
                wxPayOrder = wxPayOrderDao.findByOutTradeNo(outTradeNo);
                if (wxPayOrder == null) {
                    return FAIL;
                }
                if (StringUtils.isAnyEmpty(map.get("appid"), map.get("out_trade_no"), Price)) {
                    return FAIL;
                }
                if (!outTradeNo.equals(wxPayOrder.getOutTradeNo())) {
                    return FAIL;
                }
                if (!amount.equals(wxPayOrder.getTotalFee().toString())) {
                    return FAIL;
                }

                if (!StringUtils.equals(amount, wxPayOrder.getTotalFee().toString())) {
                    return FAIL;
                }
                wxPayOrder.setStatus(wxPayOrder.convertTradeStatus(map.get("return_code")));
                try {
                    wxPayOrderDao.save(wxPayOrder);
                } catch (Exception e) {
                    return FAIL;
                }
//                // 判断通知是否已处理，若已处理，则不予处理
//                if (map.get("time_end") == null) {
//                    // 业务处理在这里
//                    notifyStr = XMLUtil.setXML("SUCCESS", "OK");
//                }
//            } else {
//                notifyStr = XMLUtil.setXML("FAIL", "FAIL");
//            }
            }
        }
        if (payNotifyListener != null) {
            wxPayOrder.setPayfrom("wxpay");
            payNotifyListener.onSuccess(wxPayOrder);
        }
        return SUCCESS;
    }


    public Map<String, String> getOrderInfo(String outTradeNo) throws Exception {
        String xmlStr = requestWx(payConfig.getWxOrderQueryUrl(), "POST", getQueryParams(outTradeNo));
        return XMLUtil.doXMLParse(xmlStr);
    }

    public Map<String, String> getRefundInfo(String transactionId) throws Exception {
        String xmlStr = requestWx(payConfig.getWxOrderRefundQuery(), "POST", getQueryParams(transactionId));
        return XMLUtil.doXMLParse(xmlStr);
    }


    private SortedMap<String, String> getQueryParams(String outTradeNo) throws Exception {
        SortedMap<String, String> map = new TreeMap<>();
        map.put("appid", payConfig.getWxAppId());
        map.put("mch_id", payConfig.getWxMchId());
        map.put("out_trade_no", outTradeNo);
        map.put("nonce_str", PayUtil.generateNonceStr());
        String sign = PayUtil.createSign(map, payConfig.getWxApiKey());
        map.put("sign", sign);
        return map;
    }


}
