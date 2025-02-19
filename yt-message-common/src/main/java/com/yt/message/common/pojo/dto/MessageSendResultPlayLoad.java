package com.yt.message.common.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageSendResultPlayLoad {
    private Long messageId;
    private Integer result;
    private long timestamp;
    private Integer messageType;
    private Long templateId;
    private Long platformId;

    public MessageSendResultPlayLoad(Long messageId, Integer result, long timestamp, Integer messageType) {
        this.messageId = messageId;
        this.result = result;
        this.timestamp = timestamp;
        this.messageType = messageType;
    }
}