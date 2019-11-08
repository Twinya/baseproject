package com.appengine.pay.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.appengine.common.exception.EngineExceptionHelper;
import com.appengine.pay.dao.AliPayOrderDao;
import com.appengine.pay.domain.po.AliPayOrder;
import com.appengine.pay.exception.PayExceptionFactor;
import com.appengine.pay.utils.PayConfig;
import com.appengine.pay.utils.SnUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class AliPayService extends PayService {

    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";

    private final AliPayOrderDao aliPayOrderDao;

    @Autowired
    public AliPayService(AliPayOrderDao aliPayOrderDao, PayConfig payConfig) {
        this.aliPayOrderDao = aliPayOrderDao;
        this.payConfig = payConfig;
    }

    public String pay(AliPayOrder order) {

        CertAlipayRequest certAlipayRequest = getCertAlipayRequest();

        AlipayClient alipayClient = getAlipayClient(certAlipayRequest);

        AlipayTradeAppPayRequest request = getAlipayTradeAppPayRequest(order);

        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            String result = response.getBody();
            aliPayOrderDao.save(order);
            return result;
        } catch (AlipayApiException e) {
            throw EngineExceptionHelper.localException(PayExceptionFactor.PAY_FAILED, e.getErrMsg());
        } catch (Exception e) {
            throw EngineExceptionHelper.localException(PayExceptionFactor.PAY_FAILED, "orderNo 重复, 重新生成后提交");
        }
    }


    public String aliNotify(HttpServletRequest request) {
        Map<String, String> params = getNotifyParamsMap(request);
        if (checkSign(params)) {
            return updateAliPayOrder(params);
        } else {
            return FAIL;
        }
    }

    private AlipayTradeAppPayRequest getAlipayTradeAppPayRequest(AliPayOrder order) {
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody(order.getBody());
        model.setSubject(order.getSubject());
        model.setOutTradeNo(SnUtils.generateOrderNo(order.getOrderNo()));
        order.setOutTradeNo(model.getOutTradeNo());
        model.setTimeoutExpress("30m");
        model.setTotalAmount(order.getTotalAmount().toPlainString());
        request.setBizModel(model);
        request.setNotifyUrl(payConfig.getDomain() + payConfig.getAliNotifyUrl());
        return request;
    }

    private AlipayClient getAlipayClient(CertAlipayRequest certAlipayRequest) {
        //构造client
        AlipayClient alipayClient;
        try {
            alipayClient = new DefaultAlipayClient(certAlipayRequest);
        } catch (AlipayApiException e) {
            throw EngineExceptionHelper.localException(PayExceptionFactor.PAY_FAILED, e.getErrMsg());
        }
        return alipayClient;
    }

    private CertAlipayRequest getCertAlipayRequest() {
        //构造client
        CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
        //设置网关地址
        certAlipayRequest.setServerUrl(payConfig.getAliPayGateway());
        //设置应用Id
        certAlipayRequest.setAppId(payConfig.getAliAppId());
        //设置应用私钥
        certAlipayRequest.setPrivateKey(payConfig.getAliPrivateKey());
        //设置请求格式，固定值json
        certAlipayRequest.setFormat("json");
        //设置字符集
        certAlipayRequest.setCharset("utf-8");
        //设置签名类型
        certAlipayRequest.setSignType(payConfig.getAliSignType());
        //设置应用公钥证书路径
        certAlipayRequest.setCertPath(getCertPath(payConfig.getAliCertPath()));
        //设置支付宝公钥证书路径
        certAlipayRequest.setAlipayPublicCertPath(getCertPath(payConfig.getAliPublicKeyPath()));
        //设置支付宝根证书路径
        certAlipayRequest.setRootCertPath(getCertPath(payConfig.getAliRootKeyPath()));
        return certAlipayRequest;
    }

    private String getCertPath(String appCert) {
        String path;
        InputStream inputStream = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource(appCert);
            inputStream = classPathResource.getInputStream();
            File file = File.createTempFile("app", "cert");
            FileUtils.copyInputStreamToFile(inputStream, file);
            path = file.getPath();
        } catch (IOException e) {
            throw EngineExceptionHelper.localException(PayExceptionFactor.PAY_FAILED);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return path;
    }

    public Boolean checkSign(Map<String, String> params) {
        //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
        String publicKeyCertPath = getCertPath(payConfig.getAliPublicKeyPath());
        //boolean AlipaySignature.rsaCertCheckV1(Map<String, String> params, String publicKeyCertPath, String charset,String signType)
        try {
            return AlipaySignature.rsaCertCheckV1(params, publicKeyCertPath, "utf-8", payConfig.getAliSignType());
        } catch (AlipayApiException e) {
            return false;
        }

    }

    private Map<String, String> getNotifyParamsMap(HttpServletRequest request) {
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();
        for (Object o : requestParams.keySet()) {
            String name = (String) o;
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        return params;
    }

    private String updateAliPayOrder(Map<String, String> params) {
        String app_id = params.get("app_id");
        String out_trade_no = params.get("out_trade_no");
        String total_amount = params.get("total_amount");
        String trade_status = params.get("trade_status");

        if (StringUtils.isAnyEmpty(app_id, out_trade_no, total_amount, trade_status)) {
            return FAIL;
        }

        if (!payConfig.getAliAppId().equalsIgnoreCase(app_id)) {
            return FAIL;
        }

        AliPayOrder order = aliPayOrderDao.findByOutTradeNo(out_trade_no);

        if (order == null) {
            return FAIL;
        }
        if (!StringUtils.equals(total_amount, order.getTotalAmount().toPlainString())) {
            return FAIL;
        }

        order.setStatus(order.convertTradeStatus(trade_status));
        try {
            aliPayOrderDao.save(order);
        } catch (Exception e) {
            return FAIL;
        }
        if (payNotifyListener != null) {
            order.setPayfrom("alipay");
            payNotifyListener.onSuccess(order);
        }
        return SUCCESS;
    }

}
