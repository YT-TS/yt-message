package com.yt.message.handler.handler.interceptor;

import com.yt.message.common.pojo.dto.MessageSendPayload;
import com.yt.message.handler.pojo.dto.MessageSendResult;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

/**
 * @ClassName MessagePersistInterceptor
 * @Author Ts
 * @Description 持久化
 * @Version 1.0
 */
@Component
public class MessagePersistInterceptor  implements MessageHandlerInterceptor {

    @Override
    public void postHandle(MessageSendResult result, MessageExt message, MessageSendPayload messageSendPayload) {

	}

    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }
}
