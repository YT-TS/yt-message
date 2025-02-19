package com.yt.message.handler.handler.send.robot;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
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

/**
 * @ClassName WeChatRobotMessageHandler
 * @Description 微信机器人消息处理
 * @Author Ts
 * @Version 1.0
 */
@Component
@Slf4j
public class WeChatRobotMessageHandler implements MessageHandler {
    @Autowired
    private HttpClient httpClient;


    @Override
    public MessageSendResult handle(MessageExt message, MessageSendPayload messageSendPayload) {

        TemplateCache templateCache = MessageLocalCacheUtil.getTemplate(messageSendPayload.getTemplateId());
        PlatformCache platformCache = MessageLocalCacheUtil.getPlatform(templateCache.getPlatformId());

        String webhook = platformCache.getHost();
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
            log.error("发送企业微信机器人消息失败,http请求异常",e);
            return MessageSendResult.fail("发送发送企业微信机器人消息失败,http请求异常");
        }
        WeChatRobotResponse response = JSONUtil.toBean(result, WeChatRobotResponse.class);
        if (response.getErrcode() != 0){
            return MessageSendResult.fail("发送企业微信机器人发送消息失败 错误码=" + response.getErrcode() + " 错误信息=" +response.getErrmsg());
        }
        return MessageSendResult.success();
    }

    @Override
    public String getName() {
        return "wechatRobotHandler";
    }



    @Data
    static class WeChatRobotResponse {

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
