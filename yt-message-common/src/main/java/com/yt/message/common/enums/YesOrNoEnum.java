package com.yt.message.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName YesOrNoEnum
 * @Author Ts
 * @Version 1.0
 */
@AllArgsConstructor
@Getter

public enum YesOrNoEnum {
    YES(1),
    NO(0);

    private Integer value;


}
