package com.yt.message.admin.server.pojo.vo;

import com.yt.message.admin.server.pojo.dto.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName TemplatePageReqVo
 * @Author Ts
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TemplatePageReqVo extends PageParam {
    private Integer messageUsage;

    private Long platformId;
}
