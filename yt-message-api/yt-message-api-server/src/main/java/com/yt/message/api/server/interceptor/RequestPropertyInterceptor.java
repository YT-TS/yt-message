package com.yt.message.api.server.interceptor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.yt.message.api.server.exception.RequestArgAbsentException;
import com.yt.message.common.enums.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import yt.message.api.client.pojo.dto.SendMessageRequest;

import java.util.Set;

/**
 * @ClassName RequestPropertyInterceptor
 * @Description 发送请求属性拦截器
 * @Author Ts
 * @Version 1.0
 */
@Component
@Slf4j
public class RequestPropertyInterceptor implements MessageSendInterceptor {


    @Override
    public void intercept(SendMessageRequest messageRequest) {
        Long templateId = messageRequest.getTemplateId();
        if (templateId == null) {
            throw new RequestArgAbsentException("模板ID不能为空");
        }
        Integer messageType = messageRequest.getMessageType();
        MessageType type = MessageType.getByCode(messageType);
        if (type == null){
            throw new RequestArgAbsentException("消息类型不能为空");
        }
        if (type == MessageType.ROBOT){
            return;
        }

        if (messageRequest.getSingle() != null) {
            if (messageRequest.getSingle()) {
                //单个发送
                String receiveAccount = messageRequest.getReceiveAccount();
                if (StrUtil.isEmpty(receiveAccount)) {
                    throw new RequestArgAbsentException("接收账户不能为空");
                }

            } else {
                //批量发送
                Set<String> receiveAccounts = messageRequest.getReceiveAccounts();
                if (CollUtil.isEmpty(receiveAccounts)) {
                    throw new RequestArgAbsentException("接收账户不能为空");
                }
            }
        }else {
            throw new RequestArgAbsentException("接收账户不能为空");
        }
    }

    @Override
    public boolean supports(MessageType messageType) {
        return true;
    }
}
