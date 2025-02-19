package com.yt.message.common.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @ClassName MessageSendPayload
 * @Description 消息负载
 * @Author Ts
 * @Version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageSendPayload {


    /**
     * 消息id
     */
    private Long messageId;


    /**
     * 下发账户
     */
    private String receiveAccount;

    /**
     * 模板id
     */
    private Long templateId;



    /**
     * 标题参数
     */
    private String[] subjectParams;

    /**
     * 内容参数
     */
    private String[] contentParams;

    /**
     * 消息类型
     */
    private Integer messageType;

    /**
     * 发送时间戳
     */
    private long timestamp;


    private Long platformId;

    public MessageSendPayload(Long messageId, String receiveAccount, Long templateId, String[] subjectParams, String[] contentParams, Integer messageType, long timestamp) {
        this.messageId = messageId;
        this.receiveAccount = receiveAccount;
        this.templateId = templateId;
        this.subjectParams = subjectParams;
        this.contentParams = contentParams;
        this.messageType = messageType;
        this.timestamp = timestamp;
    }
}
