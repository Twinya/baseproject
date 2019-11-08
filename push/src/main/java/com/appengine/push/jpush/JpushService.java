package com.appengine.push.jpush;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;

public class JpushService {
    protected static final String CSYY_APP_KEY = "1b73158b85de31a9ef67fc04";
    protected static final String CSYYMASTER_SECRET = "d296df09e1e8c046467f2457";
    public static final String REFRESH = "refresh";

    public static void main(String[] args) {
        sendMessage("111");
    }

    public static void sendMessage(String msg) {
        ClientConfig clientConfig = ClientConfig.getInstance();
        final JPushClient jpushClient = new JPushClient(CSYYMASTER_SECRET, CSYY_APP_KEY, null, clientConfig);
        //       String authCode = ServiceHelper.getBasicAuthorization(CSYY_APP_KEY, CSYYMASTER_SECRET);
        // Here you can use NativeHttpClient or NettyHttpClient or ApacheHttpClient.
        // Call setHttpClient to set httpClient,
        // If you don't invoke this method, default httpClient will use NativeHttpClient.

        //        ApacheHttpClient httpClient = new ApacheHttpClient(authCode, null, clientConfig);
        //        NettyHttpClient httpClient =new NettyHttpClient(authCode, null, clientConfig);
        //        jpushClient.getPushClient().setHttpClient(httpClient);
        final PushPayload payload = buildPushObject_android_and_ios(msg);
        //        // For push, all you need do is to build PushPayload object.
        //        PushPayload payload = buildPushObject_all_alias_alert();
        try {
            jpushClient.sendPush(payload);
            // 如果使用 NettyHttpClient，需要手动调用 close 方法退出进程
            // If uses NettyHttpClient, call close when finished sending request, otherwise process will not exit.
            // jpushClient.close();
        } catch (APIConnectionException e) {

        } catch (APIRequestException e) {
        }
    }

    private static PushPayload buildPushObject_android_and_ios(String content) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.all())
                .setMessage(Message.content(content))
                .build();
    }

    private static PushPayload buildPushObject_android(String content) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.all())
                .setMessage(Message.content(content))
                .build();
    }

    private static PushPayload buildPushObject_ios(String content) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.all())
                .setMessage(Message.content(content))
                .build();
    }

    private static PushPayload buildPushObjectRegistrationId(String content, String... registrationId) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.registrationId(registrationId))
                .setMessage(Message.content(content))
                .build();
    }
}


