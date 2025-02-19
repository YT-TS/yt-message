package com.yt.message.admin.server.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName PreparedTemplateRspVo
 * @Author Ts
 * @Version 1.0
 */
@Data
public class PreparedTemplateRspVo {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long preparedTemplateId;

    private String templateName;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long templateId;

    private String subjectParams;


    private String contentParams;


    private String receiveAccounts;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long accountGroupId;

    private Integer requireAsync;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
