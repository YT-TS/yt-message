package com.yt.message.admin.server.pojo.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @ClassName TemplateStatusEditVo
 * @Author Ts
 * @Version 1.0
 */
@Data
public class TemplateStatusEditVo {
    @NotNull(message = "模板id不能为空")
    private Long templateId;
    @NotNull(message = "状态不能为空")
    private Integer status;
    @NotNull(message = "版本号不能为空")
    private Integer version;
}
