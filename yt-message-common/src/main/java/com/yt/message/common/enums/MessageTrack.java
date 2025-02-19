package com.yt.message.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName MessageTrack
 * @Description 消息轨迹
 * @Author Ts
 * @Version 1.0
 */
@AllArgsConstructor
@Getter
public enum MessageTrack {

    ISSUE_SUCCESS(1, "平台下发成功"),
    ISSUE_FAIL(-1, "平台下发失败"),

    SEND_SUCCESS(2, "消息发送成功"),
    SEND_FAIL(-2, "消息发送失败"),

    ;
    private Integer code;
    private String desc;

}
