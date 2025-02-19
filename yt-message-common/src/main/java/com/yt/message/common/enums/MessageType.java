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
    ROBOT(3,"机器人");

    private Integer code;

    private String desc;



    private static Map<Integer, MessageType> cache = Arrays.stream(MessageType.values()).collect(Collectors.toMap(MessageType::getCode, item->item));

    public static MessageType getByCode(Integer code) {

        return cache.get(code);
    }


}
