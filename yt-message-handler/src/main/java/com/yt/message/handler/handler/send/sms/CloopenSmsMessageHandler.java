package com.yt.message.handler.handler.send.sms;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.json.JSONUtil;
import com.yt.message.admin.client.pojo.dto.PlatformCache;
import com.yt.message.admin.client.pojo.dto.TemplateCache;
import com.yt.message.common.pojo.dto.MessageSendPayload;
import com.yt.message.handler.handler.send.MessageHandler;
import com.yt.message.handler.pojo.dto.MessageSendResult;
import com.yt.message.handler.util.MessageLocalCacheUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

/**
 * @ClassName CloopenSmsMessageHandler
 * @Description 容联云短信消息发送处理器
 * @Author Ts
 * @Version 1.0
 */
@Slf4j
@Component
public class CloopenSmsMessageHandler implements MessageHandler {
    @Autowired
    private HttpClient httpClient;

    @Override
    public MessageSendResult handle(MessageExt message, MessageSendPayload messageSendPayload) {
        TemplateCache templateCache = MessageLocalCacheUtil.getTemplate(messageSendPayload.getTemplateId());
        PlatformCache platformCache = MessageLocalCacheUtil.getPlatform(templateCache.getPlatformId());
        String timestamp = DateUtil.format(new Date(), "yyyyMMddHHmmss");
        String signParams = platformCache.getSecretId()+platformCache.getSecretKey()+ timestamp;
        String sign = MD5.create().digestHex(signParams).toUpperCase();
        String result ;
        try {
            HttpPost httpPost = new HttpPost(platformCache.getHost()+"?sig="+sign);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Authorization", Base64.encode(platformCache.getSecretId()+":"+timestamp));
            HashMap<String, Object> postBody = new HashMap<>();
            postBody.put("to", messageSendPayload.getReceiveAccount());
            postBody.put("templateId", templateCache.getPlatformTemplateId());
            postBody.put("appId", platformCache.getAppId());
            postBody.put("datas",messageSendPayload.getContentParams());
            httpPost.setEntity(new StringEntity(JSONUtil.toJsonStr(postBody)));
            result = httpClient.execute(httpPost, new BasicHttpClientResponseHandler());
        } catch (IOException e) {
            log.error("容联云短信发送消息失败,http请求异常",e);
            return MessageSendResult.fail("容联云短信发送消息失败,http请求异常: "+e.getMessage());
        }
        CloopenSmsResponse response = JSONUtil.toBean(result, CloopenSmsResponse.class);
        if (!"000000".equals(response.getStatusCode())){
            return MessageSendResult.fail("容联云短信发送消息失败 错误码=" + response.getStatusCode() + " 错误信息=" +response.getStatusMsg());
        }
        return MessageSendResult.success();
    }

    @Override
    public String getName() {
        return "cloopenSmsHandler";
    }
    @Data
    static class CloopenSmsResponse {

        /**
         * Error code
         */
        private String statusCode;

        private String statusMsg;
    }
}
