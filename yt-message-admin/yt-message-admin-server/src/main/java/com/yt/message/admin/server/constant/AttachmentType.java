package com.yt.message.admin.server.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName AttachmentType
 * @Description 附件类型
 * @Author Ts
 * @Date 2025/3/22 18:03
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum AttachmentType {

    TEXT(1,"文本"),
    IMAGE(2,"图片"),
    AUDIO(3,"音频"),
    VIDEO(4,"视频"),
    ZIP(5,"压缩包"),
    DOCUMENT(6,"DOC"),
    TABLE(7,"EXCEL"),
    PPT(8,"PPT"),
    PDF(9,"PDF"),
    OTHER(0,"其他"),

;
    public static final Map<Integer, AttachmentType> cache =
            Arrays.stream(AttachmentType.values()).collect(Collectors.toMap(AttachmentType::getCode, Function.identity()));

    public static AttachmentType getByCode(Integer code) {
        return cache.get(code);
    }
    private final Integer code;
    private final String desc;

}
