package com.yt.message.handler.handler.send;

import com.yt.message.common.pojo.dto.MessageSendPayload;
import com.yt.message.handler.pojo.dto.MessageSendResult;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * @ClassName MessageHandler
 * @Author Ts
 * @Version 1.0
 */

public interface MessageHandler {

    MessageSendResult handle(MessageExt message, MessageSendPayload messageSendPayload) ;

    String getName();

}
