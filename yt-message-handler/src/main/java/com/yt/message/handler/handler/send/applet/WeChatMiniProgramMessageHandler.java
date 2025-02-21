package com.yt.message.handler.handler.send.applet;

import cn.hutool.core.util.ArrayUtil;
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

import java.util.HashMap;

/**
 * @ClassName WeChatMiniProgramMessageHandler

 * @Author Ts
 * @Version 1.0
 */
@Component
@Slf4j
public class WeChatMiniProgramMessageHandler implements MessageHandler {
    @Autowired
    private HttpClient httpClient;
    @Override
    public MessageSendResult handle(MessageExt message, MessageSendPayload messageSendPayload) {
        //TODO 待测试
        TemplateCache templateCache = MessageLocalCacheUtil.getTemplate(messageSendPayload.getTemplateId());
        PlatformCache platformCache = MessageLocalCacheUtil.getPlatform(templateCache.getPlatformId());

        try {
            HttpPost httpPost = new HttpPost(platformCache.getHost()+"?access_token="+platformCache.getSecretKey());
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Accept", "application/json");
            HashMap<String, Object> postBodyMap = new HashMap<>();
            postBodyMap.put("template_id", templateCache.getPlatformTemplateId());
            postBodyMap.put("touser", messageSendPayload.getReceiveAccount());
            postBodyMap.put("page", templateCache.getPage());
//            postBodyMap.put("miniprogram_state","formal");
//            postBodyMap.put("lang","zh_CN");
            if (ArrayUtil.isNotEmpty(messageSendPayload.getContentParams())){
                HashMap<String, Object> paramsMap = new HashMap<>();
                for (int i = 0; i < messageSendPayload.getContentParams().length; i++) {
                    int finalI = i;
                    paramsMap.put("key"+i,new HashMap<String, Object>(){{
                        put("value",messageSendPayload.getContentParams()[finalI]);
                    }});
                }
                postBodyMap.put("data",paramsMap);
            }else {
                postBodyMap.put("data","{ }");
            }
            String postBodyStr = JSONUtil.toJsonStr(postBodyMap);
            httpPost.setEntity(new StringEntity(postBodyStr));
            String result = httpClient.execute(httpPost, new BasicHttpClientResponseHandler());
            WeChatMiniProgramResponse response = JSONUtil.toBean(result, WeChatMiniProgramResponse.class);
            if (response.getErrcode() != 0){
                return MessageSendResult.fail("微信小程序订阅消息发送失败 错误码=" + response.getErrcode() + " 错误信息=" +response.getErrmsg());
            }
            return MessageSendResult.success();
        } catch (Exception e) {
            log.error("微信小程序订阅消息消息发送失败,http请求异常",e);
            return MessageSendResult.fail("微信小程序订阅消息消息发送失败,http请求异常: "+e.getMessage());
        }

    }

    @Override
    public String getName() {
        return "weChatMiniProgramMessageHandler";
    }
    @Data
    static class WeChatMiniProgramResponse {

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
