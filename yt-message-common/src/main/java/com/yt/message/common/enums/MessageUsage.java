package com.yt.message.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName MessageUsage
 * @Description 消息用途
 * @Author Ts
 * @Version 1.0
 */
@AllArgsConstructor
@Getter
public enum MessageUsage {

    CODE(1, "验证码"),
    NOTICE(2, "通知"),
    MARKETING(3, "营销"),
    OTHER(0, "其他"),
    ;

    private Integer key;
    private String desc;
    private static Map<Integer, MessageUsage> cache = Arrays.stream(MessageUsage.values()).collect(Collectors.toMap(MessageUsage::getKey, item->item));

    public static MessageUsage getByKey(Integer key) {

        return cache.get(key);
    }
}
