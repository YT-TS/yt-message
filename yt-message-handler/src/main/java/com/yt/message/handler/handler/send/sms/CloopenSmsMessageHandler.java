package com.yt.message.handler.handler.send.sms;

import com.cloopen.rest.sdk.BodyType;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import com.yt.message.admin.client.pojo.dto.PlatformCache;
import com.yt.message.admin.client.pojo.dto.TemplateCache;
import com.yt.message.common.pojo.dto.MessageSendPayload;
import com.yt.message.handler.handler.send.MessageHandler;
import com.yt.message.handler.pojo.dto.MessageSendResult;
import com.yt.message.handler.util.MessageLocalCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

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
    @Override
    public MessageSendResult handle(MessageExt message, MessageSendPayload messageSendPayload){
        log.info("容联云短息消息处理:" + messageSendPayload.getMessageId());

        TemplateCache templateCache = MessageLocalCacheUtil.getTemplate(messageSendPayload.getTemplateId());
        PlatformCache platformCache = MessageLocalCacheUtil.getPlatform(templateCache.getPlatformId());


        //生产环境请求地址：app.cloopen.com
        String serverIp = platformCache.getHost();
        //请求端口
        String serverPort = platformCache.getPort().toString();
        //主账号,登陆云通讯网站后,可在控制台首页看到开发者主账号ACCOUNT SID和主账号令牌AUTH TOKEN
        String accountSId = platformCache.getSecretId();
        String accountToken = platformCache.getSecretKey();
        //请使用管理控制台中已创建应用的APPID
        String appId = platformCache.getAppId();
        CCPRestSmsSDK sdk = new CCPRestSmsSDK();
        sdk.init(serverIp, serverPort);
        sdk.setAccount(accountSId, accountToken);
        sdk.setAppId(appId);
        sdk.setBodyType(BodyType.Type_JSON);
        String to = messageSendPayload.getReceiveAccount();
        String templateId= templateCache.getPlatformTemplateId();
        String[] datas = messageSendPayload.getContentParams();

        HashMap<String, Object> result = sdk.sendTemplateSMS(to,templateId,datas);

        if("000000".equals(result.get("statusCode"))){
            return MessageSendResult.success();
            //正常返回输出data包体信息（map）
//            HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
//            Set<String> keySet = data.keySet();
//            for(String key:keySet){
//                Object object = data.get(key);
//                System.out.println(key +" = "+object);
//            }
        }else{
            //异常返回输出错误码和错误信息
            return MessageSendResult.fail("容联运短信发送失败 错误码="+ result.get("statusCode") +" 错误信息= "+result.get("statusMsg").toString());
//            System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
        }

    }

    @Override
    public String getName() {
        return "cloopenSmsHandler";
    }
}
