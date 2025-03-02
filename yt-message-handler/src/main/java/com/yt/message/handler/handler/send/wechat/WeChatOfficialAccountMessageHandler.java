package com.yt.message.handler.handler.send.wechat;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSONUtil;
import com.yt.message.admin.client.pojo.dto.PlatformCache;
import com.yt.message.admin.client.pojo.dto.PlatformToken;
import com.yt.message.admin.client.pojo.dto.TemplateCache;
import com.yt.message.common.pojo.dto.MessageSendPayload;
import com.yt.message.handler.handler.send.MessageHandler;
import com.yt.message.handler.pojo.dto.MessageSendResult;
import com.yt.message.handler.util.MessageLocalCacheUtil;
import com.yt.message.handler.util.PlatformTokenUtil;
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
import java.util.HashMap;

/**
 * @ClassName WeChatOfficialAccountMessageHandler
 * @Description 微信公众号模板消息发送
 * @Author Ts
 * @Version 1.0
 */
@Component
@Slf4j
public class WeChatOfficialAccountMessageHandler implements MessageHandler {
    @Autowired
    private HttpClient httpClient;

    @Override
    public MessageSendResult handle(MessageExt message, MessageSendPayload messageSendPayload) {
        TemplateCache templateCache = MessageLocalCacheUtil.getTemplate(messageSendPayload.getTemplateId());
        PlatformCache platformCache = MessageLocalCacheUtil.getPlatform(templateCache.getPlatformId());
        PlatformToken platformToken = PlatformTokenUtil.getPlatformToken(platformCache.getPlatformId());
        if (platformToken == null) {
            return MessageSendResult.fail("获取平台token失败 平台：" + platformCache.getPlatformName());
        }
        String urlStr = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + platformToken.getAccessToken();
        HttpPost httpPost = new HttpPost(urlStr);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Accept", "application/json");
        HashMap<String, Object> postBodyMap = new HashMap<>();
        postBodyMap.put("template_id", templateCache.getPlatformTemplateId());
        postBodyMap.put("touser", messageSendPayload.getReceiveAccount());
        if (ArrayUtil.isNotEmpty(messageSendPayload.getContentParams())){
            postBodyMap.put("data", new HashMap<String, Object>() {{
                for (int i = 0; i < messageSendPayload.getContentParams().length; i++) {
                    final String keyword = messageSendPayload.getContentParams()[i];
                    put("keyword" + (i+1), new HashMap<String, Object>(){{
                        put("value", keyword);
                    }});
                }
            }});
        }
        httpPost.setEntity(new StringEntity(JSONUtil.toJsonStr(postBodyMap)));
        try {
            String result = httpClient.execute(httpPost, new BasicHttpClientResponseHandler());
            WeChatOfficialAccountMessageResponse response = JSONUtil.toBean(result, WeChatOfficialAccountMessageResponse.class);
            if (response.getErrcode() != 0){
                return MessageSendResult.fail("微信公众号模板消息发送消息失败 错误码=" + response.getErrcode() + " 错误信息=" +response.getErrmsg());
            }
            return MessageSendResult.success();
        } catch (IOException e) {
            log.error("微信公众号模板消息发送消息失败,http请求异常", e);
            return MessageSendResult.fail("微信公众号模板消息发送消息失败,http请求异常: "+e.getMessage());
        }

    }

    @Override
    public String getName() {
        return "weChatOfficialAccountMessageHandler";
    }
    @Data
    static class WeChatOfficialAccountMessageResponse {

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
