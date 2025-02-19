package com.yt.message.handler.handler.interceptor;

import com.yt.message.common.pojo.dto.MessageSendPayload;
import com.yt.message.handler.pojo.dto.MessageSendResult;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.lang.Nullable;

/**
 * @ClassName MessageHandlerInterceptor
 * @Description 消息处理拦截器
 * @Author Ts
 * @Version 1.0
 */

public interface MessageHandlerInterceptor  {

	/**
	*@author Ts
	*@Description 前置处理 返回失败的处理结果时，消息将不会被消息发送处理器处理
	*@Param [message]
	*@Param [org.apache.rocketmq.common.message.MessageExt]
	*@Return boolean
	**/
	default HandleResult preHandle(MessageExt message, MessageSendPayload messageSendPayload){
		return HandleResult.success();
	}


	/**
	*@author Ts
	*@Description 后置处理 在消息处理完后执行，即使在前置处理时返回失败，也会执行本方法(此时result为失败结果)
	*@Param [result, message]
	*@Param [com.yt.message.consumer.domain.MessageSendResult, org.apache.rocketmq.common.message.MessageExt]
	*@Return void
	**/
	default void postHandle(MessageSendResult result, MessageExt message, MessageSendPayload messageSendPayload)  {

	}



	/**
	*@author Ts
	*@Description 最终处理 finalHandle 无论怎样都会执行
	*@Param [ex]
	*@Param [java.lang.Exception]
	*@Return void
	**/
	default void finalHandle(MessageExt message, MessageSendPayload messageSendPayload, @Nullable MessageSendResult result, @Nullable Exception ex)  {
	}

	/**
	*@author Ts
	*@Description 拦截器执行顺序 越小越先执行
	*@Param []
	*@Return int
	**/
	int order();

}
