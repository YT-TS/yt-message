package com.yt.message.handler.handler.send.applet;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.crypto.SignUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.json.JSONUtil;
import com.yt.message.admin.client.pojo.dto.PlatformCache;
import com.yt.message.admin.client.pojo.dto.TemplateCache;
import com.yt.message.common.pojo.dto.MessageSendPayload;
import com.yt.message.handler.handler.send.MessageHandler;
import com.yt.message.handler.pojo.dto.MessageSendResult;
import com.yt.message.handler.util.MessageLocalCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @ClassName AlipayMiniProgramMessageHandler
 * @Description 支付宝小程序
 * @Author Ts
 * @Version 1.0
 */
@Component
@Slf4j
public class AlipayMiniProgramMessageHandler implements MessageHandler {
    @Autowired
    private HttpClient httpClient;
    @Override
    public MessageSendResult handle(MessageExt message, MessageSendPayload messageSendPayload) {
        //TODO 待测试
        TemplateCache templateCache = MessageLocalCacheUtil.getTemplate(messageSendPayload.getTemplateId());
        PlatformCache platformCache = MessageLocalCacheUtil.getPlatform(templateCache.getPlatformId());

        try {
            HttpPost httpPost = new HttpPost(platformCache.getHost());

            httpPost.setHeader("Content-Type", "application/json");
            // 构建postBody
            HashMap<String, Object> postBody = new HashMap<>();
            postBody.put("user_template_id", templateCache.getPlatformTemplateId());
            if (messageSendPayload.getReceiveAccount().startsWith("2088")){
                postBody.put("to_user_id", messageSendPayload.getReceiveAccount());
            }else {
                postBody.put("to_open_id", messageSendPayload.getReceiveAccount());
            }
            postBody.put("page",templateCache.getPage());
            if (ArrayUtil.isNotEmpty(messageSendPayload.getContentParams())){
                HashMap<String, Object> paramsMap = new HashMap<>();
                for (int i = 0; i < messageSendPayload.getContentParams().length; i++) {
                    int finalI = i;
                    paramsMap.put("keyword"+i,new HashMap<String, Object>(){{
                        put("value",messageSendPayload.getContentParams()[finalI]);
                    }});
                }
                postBody.put("data",paramsMap);
            }else {
                postBody.put("data","{ }");
            }
            String postBodyStr = JSONUtil.toJsonStr(postBody);
            httpPost.setEntity(new StringEntity(postBodyStr));
            // 签名
            httpPost.setHeader("authorization", getAuthorization(platformCache.getAppId(), platformCache.getHost(),postBodyStr, platformCache.getSecretKey()));
            //响应码大于等于300会抛出异常
            httpClient.execute(httpPost, new BasicHttpClientResponseHandler());
        } catch (Exception e) {
            log.error("支付宝小程序模板消息发送消息失败,http请求异常", e);
            return MessageSendResult.fail("支付宝小程序模板消息发送消息失败,http请求异常: "+e.getMessage());
        }


        return MessageSendResult.success();
    }

    @Override
    public String getName() {
        return "alipayMiniProgramMessageHandler";
    }


    private String getAuthorization(String appId, String url, String postBody, String privateKey) {
        HashMap<String, String> map = new HashMap<>();
        map.put("app_id", appId);
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("nonce", UUID.randomUUID().toString());
        String authString = map.entrySet().stream().map(item ->
                item.getKey() + "=" + item.getValue()
        ).collect(Collectors.joining(","));
        String httpMethod = "POST";
        String httpRequestUrl = URLUtil.getPath(url);
        String content = StrUtil.join("\n", authString, httpMethod, httpRequestUrl, postBody);
        Sign sign = SignUtil.sign(SignAlgorithm.SHA256withRSA, privateKey, null);
        String signContent = Base64.encode(sign.sign(content));

        return "ALIPAY-SHA256withRSA " + authString + "," + signContent;
    }
}
