package com.yt.message.api.server.service;

import com.yt.message.common.pojo.dto.MessageSendPayload;
import com.yt.message.mq.constant.MQConstant;
import com.yt.message.log.utils.BizLogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import yt.message.api.client.pojo.dto.MessageSendRsp;

import java.util.Collection;

import static com.yt.message.common.enums.MessageTrack.*;

/**
 * @ClassName MqMessageLogEnhanceService
 * @Author Ts
 * @Version 1.0
 */
@Service
@Slf4j
public class MqSendMessageWithLogEnhanceService {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public MessageSendRsp asyncSend(Message<MessageSendPayload> message) {

        rocketMQTemplate.asyncSend(MQConstant.MESSAGE_TOPIC_NAME, message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                if (sendResult.getSendStatus() != SendStatus.SEND_OK) {
                    //发送成功 但出了点小状况
                    log.warn(sendResult.getSendStatus().toString());
                }
                BizLogUtils.log( message.getPayload(), ISSUE_SUCCESS);
            }

            @Override
            public void onException(Throwable e) {
                log.error("消息发送失败,消息Id：{}", message.getPayload().getMessageId(), e);
                BizLogUtils.log( message.getPayload(), ISSUE_FAIL,e);
            }
        });
        return MessageSendRsp.success();
    }



    public MessageSendRsp syncSend(Message<MessageSendPayload> message) {
        try {
            //业务日志

            SendResult sendResult = rocketMQTemplate.syncSend(MQConstant.MESSAGE_TOPIC_NAME, message);
            if (sendResult.getSendStatus() != SendStatus.SEND_OK) {
                //发送成功 但出了点小状况
                log.warn(sendResult.getSendStatus().toString());
            }
            //业务日志
            BizLogUtils.log( message.getPayload(), ISSUE_SUCCESS);
            return MessageSendRsp.success();
        } catch (Exception e) {
            //错误日志
            log.error("消息发送失败,消息Id：{}", message.getPayload().getMessageId(), e);
            //有任务日志
            BizLogUtils.log( message.getPayload(), ISSUE_FAIL,e);
            return MessageSendRsp.fail("发送失败，联系管理员");
        }

    }


    public MessageSendRsp asyncSend(Collection<Message<MessageSendPayload>> messages) {

       rocketMQTemplate.asyncSend(MQConstant.MESSAGE_TOPIC_NAME, messages, new SendCallback(){
           @Override
           public void onSuccess(SendResult sendResult) {
               if (sendResult.getSendStatus() != SendStatus.SEND_OK) {
                   //发送成功 但出了点小状况
                   log.warn(sendResult.getSendStatus().toString());
               }
               messages.forEach(message -> BizLogUtils.log( message.getPayload(), ISSUE_SUCCESS));
           }
           @Override
           public void onException(Throwable e) {
               log.error("消息发送失败,批量发送",  e);
               messages.forEach(message -> BizLogUtils.log( message.getPayload(), ISSUE_FAIL,e));
           }
       });
        return MessageSendRsp.success();

    }
    public MessageSendRsp syncSend(Collection<Message<MessageSendPayload>> messages) {
        try {
            SendResult sendResult = rocketMQTemplate.syncSend(MQConstant.MESSAGE_TOPIC_NAME, messages);
            if (sendResult.getSendStatus() != SendStatus.SEND_OK) {
                //发送成功 但出了点小状况
                log.warn(sendResult.getSendStatus().toString());
            }
            messages.forEach(message -> BizLogUtils.log( message.getPayload(), ISSUE_SUCCESS));
            return MessageSendRsp.success();
        } catch (Exception e) {
            log.error("消息发送失败,批量发送",  e);
            messages.forEach(message -> BizLogUtils.log( message.getPayload(), ISSUE_FAIL,e));
            return MessageSendRsp.fail("发送失败，联系管理员");
        }
    }

}
