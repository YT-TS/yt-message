package com.yt.message.handler.handler.interceptor;

import com.yt.message.common.enums.MessageTrack;
import com.yt.message.common.enums.ResultCodeEnum;
import com.yt.message.common.pojo.dto.MessageSendPayload;
import com.yt.message.handler.pojo.dto.MessageSendResult;
import com.yt.message.log.utils.BizLogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * @ClassName MessageSendBizLogInterceptor
 * @Description 发送业务日志打印
 * @Author Ts
 * @Version 1.0
 */
@Slf4j
@Component
public class MessageSendBizLogInterceptor implements MessageHandlerInterceptor {
    @Override
    public void finalHandle(MessageExt message, MessageSendPayload messageSendPayload, @Nullable MessageSendResult result, @Nullable Exception ex) {
        if (result != null) {
            if (result.getCode() == ResultCodeEnum.FAIL.getCode()) {
                //发送失败业务日志
                BizLogUtils.log( messageSendPayload, MessageTrack.SEND_FAIL, result.getMsg());
            } else {
                //发送成功业务日志
                BizLogUtils.log( messageSendPayload, MessageTrack.SEND_SUCCESS);
            }
        }else {
            //发送失败业务日志
            BizLogUtils.log( messageSendPayload, MessageTrack.SEND_FAIL,ex);
        }


    }

    @Override
    public int order() {
        return Integer.MIN_VALUE;
    }


}
