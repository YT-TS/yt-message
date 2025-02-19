package com.yt.message.handler.handler.send.robot;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
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

import java.nio.charset.StandardCharsets;

/**
 * @ClassName FeiShuRobotMessageHandler
 * @Description 飞书机器人消息处理
 * @Author Ts
 * @Version 1.0
 */
@Component
@Slf4j
public class FeiShuRobotMessageHandler implements MessageHandler {
    @Autowired
    private HttpClient httpClient;

    @Override
    public MessageSendResult handle(MessageExt message, MessageSendPayload messageSendPayload)  {
        TemplateCache templateCache = MessageLocalCacheUtil.getTemplate(messageSendPayload.getTemplateId());
        PlatformCache platformCache = MessageLocalCacheUtil.getPlatform(templateCache.getPlatformId());


        String webhook = platformCache.getHost();
        String content = templateCache.getContent();

        if (ArrayUtil.isNotEmpty(messageSendPayload.getContentParams())) {
            content = StrUtil.format(templateCache.getContent(), (Object[]) messageSendPayload.getContentParams());
        }


        if (StrUtil.isNotEmpty(platformCache.getSecretKey())) {
            Long timestamp = System.currentTimeMillis() / 1000;
            String sign = getSignature(timestamp, platformCache.getSecretKey());
            content = JSONUtil.parseObj(content).set("timestamp", timestamp).set("sign", sign).toString();
        }


        String result ;
        try {
            HttpPost httpPost = new HttpPost(webhook);
            httpPost.setEntity(new StringEntity(content));
            httpPost.setHeader("Content-Type", "application/json");
            result = httpClient.execute(httpPost, new BasicHttpClientResponseHandler());


        } catch (Exception e) {
            log.error("发送飞书机器人消息失败,http请求异常",e);
            return MessageSendResult.fail("发送飞书机器人消息失败,http请求异常");
        }

        FeiShuRobotResponse response = JSONUtil.toBean(result, FeiShuRobotResponse.class);
        if (response.getCode() != 0) {
            return MessageSendResult.fail("飞书机器人发送消息失败 错误码=" + response.getCode() + " 错误信息=" + response.getMsg());
        }

        return MessageSendResult.success();
    }

    @Override
    public String getName() {
        return "feiShuRobotHandler";
    }

    private static String getSignature(Long timestamp, String secretKey) {
        String stringToSign = timestamp + "\n" + secretKey;
        HMac hMac = new HMac(HmacAlgorithm.HmacSHA256, stringToSign.getBytes(StandardCharsets.UTF_8));
        byte[] digest = hMac.digest(new byte[]{});
        return Base64.encode(digest);
    }


    @Data
    static class FeiShuRobotResponse {

        /**
         * Error code
         */
        private Integer code;

        /**
         * Error message
         */
        private String msg;
    }
}
