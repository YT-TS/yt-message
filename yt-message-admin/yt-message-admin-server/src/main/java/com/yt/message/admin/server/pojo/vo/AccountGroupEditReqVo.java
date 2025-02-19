package com.yt.message.admin.server.pojo.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

/**
 * @ClassName AccountGroupEditReqVo
 * @Author Ts
 * @Version 1.0
 */
@Data
public class AccountGroupEditReqVo {
    @NotNull(message = "账户组id不能为空")
    private Long groupId;
    /**
     * 账户组名称
     */
    @NotBlank(message = "账户组名称不能为空")
    private String groupName;
    /**
     * 账户，每个账户用,隔开
     */
    @NotEmpty(message = "账户不能为空")
    private Set<String> members;
    /**
     * 账户类型
     */
    @NotNull(message = "账户类型不能为空")
    private Integer type;

}
