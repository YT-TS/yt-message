package com.yt.message.handler.handler.send.robot;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.net.URLEncodeUtil;
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
 * @ClassName DingDingRobotMessageHandler
 * @Description 钉钉机器人消息发送处理
 * @Author Ts
 * @Version 1.0
 */
@Component
@Slf4j
public class DingDingRobotMessageHandler implements MessageHandler {

    @Autowired
    private HttpClient httpClient;

    @Override
    public MessageSendResult handle(MessageExt message, MessageSendPayload messageSendPayload) {
        TemplateCache templateCache = MessageLocalCacheUtil.getTemplate(messageSendPayload.getTemplateId());
        PlatformCache platformCache = MessageLocalCacheUtil.getPlatform(templateCache.getPlatformId());

        String webhook = platformCache.getHost();
        if (StrUtil.isNotEmpty(platformCache.getSecretKey())){
            Long timestamp = System.currentTimeMillis();
            String sign = getSignature(timestamp, platformCache.getSecretKey());
            webhook = webhook + "&timestamp=" + timestamp + "&sign=" + sign;
        }

        String content = templateCache.getContent();

        if (ArrayUtil.isNotEmpty(messageSendPayload.getContentParams())) {
            content = StrUtil.format(templateCache.getContent(), (Object[]) messageSendPayload.getContentParams());
        }
        String result ;
        try {
            HttpPost httpPost = new HttpPost(webhook);
            httpPost.setEntity(new StringEntity(content));
            httpPost.setHeader("Content-Type", "application/json");
            result = httpClient.execute(httpPost, new BasicHttpClientResponseHandler());
        } catch (Exception e) {
           log.error("钉钉机器人发送消息失败,http请求异常",e);
           return MessageSendResult.fail("钉钉机器人发送消息失败,http请求异常: "+e.getMessage());
        }

        DingDingRobotResponse response = JSONUtil.toBean(result, DingDingRobotResponse.class);
        if (response.getErrcode() != 0){
            return MessageSendResult.fail("钉钉机器人发送消息失败 错误码=" + response.getErrcode() + " 错误信息=" +response.getErrmsg());
        }
        return MessageSendResult.success();
    }

    @Override
    public String getName() {
        return "dingDingRobotHandler";
    }

    private static String getSignature(Long timestamp, String secretKey) {
        String stringToSign = timestamp + "\n" + secretKey;
        HMac hMac = new HMac(HmacAlgorithm.HmacSHA256, secretKey.getBytes(StandardCharsets.UTF_8));
        byte[] digest = hMac.digest(stringToSign.getBytes(StandardCharsets.UTF_8));
        return URLEncodeUtil.encode(Base64.encode(digest));

    }

    @Data
    static class DingDingRobotResponse {

        /**
         * Error code
         */
        private Integer errcode;

        /**
         * Error message
         */
        private String errmsg;
    }

}
