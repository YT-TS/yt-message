package com.yt.message.admin.server.pojo.vo;

import com.yt.message.admin.server.pojo.dto.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName MessageRecordPageReqVo

 * @Author Ts
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MessageRecordPageReqVo extends PageParam {
    private Long startTimeStamp;
    private Long endTimeStamp;
    private Long templateId;
    private Long messageId;
    private String receiveAccount;



}
