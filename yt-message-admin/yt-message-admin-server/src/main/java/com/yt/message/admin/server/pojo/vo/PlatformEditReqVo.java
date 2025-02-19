package com.yt.message.admin.server.pojo.vo;

import com.yt.message.admin.server.pojo.vo.validation.EditPlatformGroupSequenceProvider;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.group.GroupSequenceProvider;

/**
 * @ClassName PlatformEditReqVo
 * @Author Ts
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@GroupSequenceProvider(EditPlatformGroupSequenceProvider.class)
public class PlatformEditReqVo extends PlatformReqVo {
    /**
     * 平台id
     */
    @NotNull(message = "平台id不能为空")
    private Long platformId;

    @NotNull(message = "版本号不能为空")
    private Integer version;


}


