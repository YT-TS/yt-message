package com.yt.message.handler.handler.send.wechat;

import com.yt.message.admin.client.pojo.dto.PlatformCache;
import com.yt.message.admin.client.pojo.dto.PlatformToken;
import com.yt.message.admin.client.pojo.dto.TemplateCache;
import com.yt.message.common.pojo.dto.MessageSendPayload;
import com.yt.message.handler.handler.send.MessageHandler;
import com.yt.message.handler.pojo.dto.MessageSendResult;
import com.yt.message.handler.util.MessageLocalCacheUtil;
import com.yt.message.handler.util.PlatformTokenUtil;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
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
public class WeChatOfficialAccountMessageHandler implements MessageHandler {
    @Autowired
    private HttpClient httpClient;
    @Override
    public MessageSendResult handle(MessageExt message, MessageSendPayload messageSendPayload) {
        TemplateCache templateCache = MessageLocalCacheUtil.getTemplate(messageSendPayload.getTemplateId());
        PlatformCache platformCache = MessageLocalCacheUtil.getPlatform(templateCache.getPlatformId());
        PlatformToken platformToken = PlatformTokenUtil.getPlatformToken(platformCache.getPlatformId());
        if (platformToken == null){
            return MessageSendResult.fail("获取平台token失败 平台id：" + platformCache.getPlatformId());
        }




        return null;
    }

    public static void main(String[] args) throws IOException {


        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN");
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Accept", "application/json");
        HashMap<String, Object> postBodyMap = new HashMap<>();
        postBodyMap.put("template_id", "y_guyvvm0L84-nVcrKAZoJ7tSIGsZgRvpKlqb27H1ug");
        postBodyMap.put("touser", "oKM1S7P5puNGzZCp-D7Ogp6AalIY");
        postBodyMap.put("data", new HashMap<String, Object>(){{
            put("keyword1", new HashMap<String, Object>(){{
                put("value", "111");
            }});
            put("keyword2", new HashMap<String, Object>(){{
                put("value", "222");
            }});
            put("keyword3", new HashMap<String, Object>(){{
                put("value", "333");
            }});

        }});
        String execute = client.execute(httpPost, new BasicHttpClientResponseHandler());
        System.out.println(execute);


        client.close();
    }

    @Override
    public String getName() {
        return "weChatOfficialAccountMessageHandler";
    }
}
