package com.yt.message.api.server.interceptor;

import com.yt.message.common.enums.MessageType;
import org.springframework.stereotype.Component;
import yt.message.api.client.pojo.dto.SendMessageRequest;

/**
 * @ClassName RobotMessageSendInterceptor
 * @Description 机器人消息校验
 * @Author Ts
 * @Version 1.0
 */
@Component
public class RobotMessageSendInterceptor implements MessageSendInterceptor{
    @Override
    public void intercept(SendMessageRequest messageRequest) {

    }

    @Override
    public boolean supports(MessageType messageType) {
        return MessageType.ROBOT == messageType;
    }
}
