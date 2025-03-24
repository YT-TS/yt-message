package com.yt.message.admin.server.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName AttachmentRspVo
 * @Description TODO
 * @Author Ts
 * @Version 1.0
 */
@Data
public class AttachmentRspVo {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long attachmentId;
    /**
     * 原始文件名
     */
    private String originName;

    /**
     * 文件类型
     */
    private Integer type;
    /**
     * 文件大小 单位字节
     */

    private Long size;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
