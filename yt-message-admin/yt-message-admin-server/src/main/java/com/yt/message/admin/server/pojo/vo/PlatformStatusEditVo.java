package com.yt.message.admin.server.pojo.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @ClassName PlatformStatusEditVo
 * @Author Ts
 * @Version 1.0
 */
@Data
public class PlatformStatusEditVo {
    /**
     * 平台id
     */
    @NotNull(message = "平台id不能为空")
    private Long platformId;
    @NotNull(message = "版本号不能为空")
    private Integer version;
    @NotNull(message = "激活状态不能为空")
    private Integer status;



}
