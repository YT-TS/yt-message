package com.yt.message.admin.server.constant;

import com.yt.message.common.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @ClassName AccountType
 * @Description 账户类型
 * @Author Ts
 * @Version 1.0
 */
@AllArgsConstructor
@Getter
public enum AccountType {
    PHONE(MessageType.SMS.getCode(), "手机号"),
    EMAIL(MessageType.EMAIL.getCode(), "邮箱号")
    ;
    private Integer code;
    private String desc;

    public static AccountType getByCode(Integer code) {
        return Arrays.stream(AccountType.values()).filter(accountType -> accountType.getCode().equals(code)).findFirst().orElse(null);
    }


}
