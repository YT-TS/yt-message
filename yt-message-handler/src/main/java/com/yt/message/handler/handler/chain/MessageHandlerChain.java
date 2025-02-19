package com.yt.message.handler.handler.chain;

import cn.hutool.json.JSONUtil;
import com.yt.message.common.enums.YesOrNoEnum;
import com.yt.message.common.pojo.dto.MessageSendPayload;
import com.yt.message.handler.handler.interceptor.HandleResult;
import com.yt.message.handler.handler.interceptor.MessageHandlerInterceptor;
import com.yt.message.handler.handler.send.MessageHandler;
import com.yt.message.handler.pojo.dto.MessageSendResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @ClassName MessageHandlerChain1
 * @Author Ts
 * @Version 1.0
 */
@Slf4j
public class MessageHandlerChain {


    private final List<MessageHandlerInterceptor> messageSendInterceptorList = new LinkedList<>();
    private final MessageHandler messageHandler;


    public MessageHandlerChain addMessageSendInterceptor(MessageHandlerInterceptor messageSendInterceptor) {
        messageSendInterceptorList.add(messageSendInterceptor);
        sort();
        return this;
    }

    public void addMessageSendInterceptors(List<MessageHandlerInterceptor> messageSendInterceptors) {
        messageSendInterceptorList.addAll(messageSendInterceptors);
        sort();
    }


    public MessageHandlerChain(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;

    }

    private void sort() {
        messageSendInterceptorList.sort(Comparator.comparingInt(MessageHandlerInterceptor::order));
    }


    public void handleMessage(MessageExt message) {
        Exception ex = null;
        //此次消息发送结果
        MessageSendResult sendResult = null;
        MessageSendPayload messageSendPayload = null;
        try {
            messageSendPayload = JSONUtil.toBean(new String(message.getBody()), MessageSendPayload.class);
            //消息消费前置处理
            for (MessageHandlerInterceptor messageSendInterceptor : messageSendInterceptorList) {
                //前置处理结果
                HandleResult preHandleResult = messageSendInterceptor.preHandle(message, messageSendPayload);
                if (!preHandleResult.getResult()) {
                    //当返回失败结果 跳出循环并为消息发送结果赋值失败结果
                    sendResult = MessageSendResult.fail(preHandleResult.getMsg());
                    break;
                }
            }
            //FORTEST
            String forTest = message.getProperty("forTest");
            if (YesOrNoEnum.YES.getValue().toString().equals(forTest)) {
                Random random = new Random();
                int restlt = random.nextInt(100);
                if (restlt >= 97) {
                    sendResult = MessageSendResult.fail("测试失败");
                } else {
                    sendResult = MessageSendResult.success("测试成功");
                }
            }

            if (sendResult == null) {
                //前置处理未返回失败 则消息发送
                sendResult = messageHandler.handle(message, messageSendPayload);
            }
            //消息消费后置处理
            for (MessageHandlerInterceptor messageSendInterceptor : messageSendInterceptorList) {
                messageSendInterceptor.postHandle(sendResult, message, messageSendPayload);
            }

        } catch (Exception e) {
            ex = e;
            log.error("消息发送异常,消息id:{}", messageSendPayload == null ? "未知id" : messageSendPayload.getMessageId(), e);
            //之所以往外抛出 是考虑到可能设置了重试机制
            throw e;
        } finally {
            for (MessageHandlerInterceptor messageSendInterceptor : messageSendInterceptorList) {
                messageSendInterceptor.finalHandle(message, messageSendPayload, sendResult, ex);
            }
        }
    }
}
