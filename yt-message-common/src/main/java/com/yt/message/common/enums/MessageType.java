package com.yt.message.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName MessageType
 * @Description 消息类型
 * @Author Ts
 * @Version 1.0
 */

@AllArgsConstructor
@Getter
public enum MessageType {

//code 不要小于0
    SMS(1, "短信"),
    EMAIL(2,"邮箱"),
    ROBOT(3,"机器人"),
//    MINI_PROGRAM(4,"小程序订阅消息"),
    WECHAT_OFFICIAL_ACCOUNT_TEMPLATE_MESSAGE(5,"微信公众号模板消息"),
    ;

    private Integer code;

    private String desc;



    private static final Map<Integer, MessageType> cache = Arrays.stream(MessageType.values()).collect(Collectors.toMap(MessageType::getCode, item->item));

    public static MessageType getByCode(Integer code) {

        return cache.get(code);
    }


}
