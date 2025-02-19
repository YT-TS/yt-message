package com.yt.message.api.server.interceptor;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import com.yt.message.api.server.exception.RequestArgFormatException;
import com.yt.message.common.enums.MessageType;
import org.springframework.stereotype.Component;
import yt.message.api.client.pojo.dto.SendMessageRequest;

/**
 * @ClassName EmailMessageSendInterceptor
 * @Description email消息校验
 * @Author Ts
 * @Version 1.0
 */
@Component
public class EmailMessageSendInterceptor implements MessageSendInterceptor {


    @Override
    public void intercept(SendMessageRequest messageRequest) {
        if (StrUtil.isNotEmpty(messageRequest.getReceiveAccount())){
            if (!Validator.isEmail(messageRequest.getReceiveAccount())){
                throw new RequestArgFormatException("接收账户格式错误");
            }
        }
        if (CollUtil.isNotEmpty(messageRequest.getReceiveAccounts())){
            boolean match = messageRequest.getReceiveAccounts().stream().anyMatch(receiveAccount -> !Validator.isEmail(receiveAccount));
            if (match){
                throw new RequestArgFormatException("接收账户格式错误");
            }
        }

    }

    @Override
    public boolean supports(MessageType messageType) {
        return MessageType.EMAIL == messageType;
    }


}
