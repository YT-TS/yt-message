package com.yt.message.admin.server.pojo.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @ClassName MessageSendVo
 * @Author Ts
 * @Version 1.0
 */
@Data

public class MessageSendVo {
    @NotNull(message = "模板id不能为空")
    private Long templateId;

    private String receiveAccounts;
    private Long accountGroupId;



    @NotNull(message = "是否异步不能为空")
    private Integer requireAsync;


    private String[] contentParams;


    private String[] subjectParams;




}
