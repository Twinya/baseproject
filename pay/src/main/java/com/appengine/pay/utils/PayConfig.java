package com.appengine.pay.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
public class PayConfig {

    public enum SignType {
        MD5, HMACSHA256
    }

    @Value("${pay.domain}")
    private String domain;

    /**
     * ali config
     */
    @Value("${pay.ali.gateway}")
    private String aliPayGateway;
    @Value("${pay.ali.signType}")
    private String aliSignType;
    @Value("${pay.ali.appId}")
    private String aliAppId;
    @Value("${pay.ali.appCertPath}")
    private String aliCertPath;
    @Value("${pay.ali.publicKeyPath}")
    private String aliPublicKeyPath;
    @Value("${pay.ali.rootKeyPath}")
    private String aliRootKeyPath;
    @Value("${pay.ali.privateKey}")
    private String aliPrivateKey;
    @Value("${pay.ali.notifyUrl}")
    private String aliNotifyUrl;
    @Value("${pay.ali.publicKey}")
    private String aliPublicKey;
    @Value("${pay.ali.aesSecret}")
    private String aliAesSecret;

    /**
     * wx config
     */
    @Value("${pay.wx.appId}")
    private String wxAppId;
    @Value("${pay.wx.apiKey}")
    private String wxApiKey;
    @Value("${pay.wx.appCertPath}")
    private String wxCertPath;
    @Value("${pay.wx.notifyUrl}")
    private String wxNotifyUrl;
    @Value("${pay.wx.mchId}")
    private String wxMchId;
    @Value("${pay.wx.gateUrl}")
    private String wxGateUrl;
    @Value("${pay.wx.orderQueryUrl}")
    private String wxOrderQueryUrl;
    @Value("${pay.wx.closeOrderUrl}")
    private String wxCloseOrderUrl;
    @Value("${pay.wx.orderRefund}")
    private String wxOrderRefund;
    @Value("${pay.wx.orderRefundQuery}")
    private String wxOrderRefundQuery;

}
