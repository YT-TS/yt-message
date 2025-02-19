package com.yt.message.admin.server.pojo.vo;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName PreparedTemplateReqVo
 * @Author Ts
 * @Version 1.0
 */
@Data
public class PreparedTemplateReqVo {

    /**
     * 预发送消息模板名称
     */
    @NotBlank(message = "模板名称不能为空")
    private String templateName;
    /**
     * 消息内容模板id
     */
    @NotNull(message = "内容模板不能为空")
    private Long templateId;

    /**
     * 账户/消息类型
     */
    @NotNull(message = "账户类型/消息类型不能为空")
    private Integer messageType;


    private List<String> subjectParams;


    private List<String> contentParams;



    private String receiveAccounts;


    private Long accountGroupId;

    @NotNull(message = "是否异步不能为空")
    private Integer requireAsync;
}
