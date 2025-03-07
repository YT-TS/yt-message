package com.yt.message.log.utils;

import com.yt.message.common.enums.MessageTrack;
import com.yt.message.common.pojo.dto.MessageSendPayload;
import com.yt.message.log.filter.BizMarker;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.spi.LoggingEventBuilder;

/**
 * @ClassName BizLogUtils
 * @Author Ts
 * @Version 1.0
 */
@Slf4j
public class BizLogUtils {

    public static final Marker bizMarker = new BizMarker();
    private static final String BIZ_ID_FIELD = "biz_id";
    private static final String MESSAGE_TRACK_FIELD = "step";
    private static final String HAND_TIMESTAMP_FIELD = "hand_timestamp";
    private static final String RECEIVE_ACCOUNT_FIELD = "receive_account";
    private static final String TEMPLATE_ID_FIELD = "template_id";
    private static final String NOTE = "note";

    public static void log(MessageSendPayload messageSendPayload, MessageTrack messageTrack) {
        log(messageSendPayload, messageTrack,null,null);
    }
    public static void log(MessageSendPayload messageSendPayload, MessageTrack messageTrack, Throwable e) {
        log(messageSendPayload, messageTrack,null,e);
    }
    public static void log(MessageSendPayload messageSendPayload, MessageTrack messageTrack,String note) {
        log(messageSendPayload, messageTrack,note,null);
    }


    public static void log(MessageSendPayload messageSendPayload, MessageTrack messageTrack,String note, Throwable e) {
        LoggingEventBuilder builder = log.atInfo().addKeyValue(BIZ_ID_FIELD, messageSendPayload.getMessageId().toString())
                .addKeyValue(MESSAGE_TRACK_FIELD, messageTrack.getCode())
                .addKeyValue(HAND_TIMESTAMP_FIELD, System.currentTimeMillis())
                .addKeyValue(RECEIVE_ACCOUNT_FIELD, messageSendPayload.getReceiveAccount())
                .addKeyValue(TEMPLATE_ID_FIELD, messageSendPayload.getTemplateId())
                .addMarker(bizMarker);
        String errMsg = null;
        if (note != null) {
            errMsg = note;
            builder.addKeyValue(NOTE, note);
        }else if (e != null) {
            StackTraceElement[] stackTrace = e.getStackTrace();
            String exNote = e + "\n" + stackTrace[0];
            errMsg = exNote;
            builder.addKeyValue(NOTE, exNote);
        }

        if(messageTrack.equals(MessageTrack.ISSUE_FAIL) || messageTrack.equals(MessageTrack.SEND_FAIL)){
            builder.log(messageTrack.getDesc() + errMsg );
            return;
        }
        builder.log(messageTrack.getDesc());

    }


}
