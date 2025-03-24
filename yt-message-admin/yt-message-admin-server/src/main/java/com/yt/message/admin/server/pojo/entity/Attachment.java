package com.yt.message.admin.server.pojo.entity;

/**
 * @ClassName Attachment
 * @Description 消息附件
 * @Author Ts
 * @Version 1.0
 */


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 接收账户组
 * </p>
 *
 * @author yt
 * @since 2025-01-09
 */
@EqualsAndHashCode(callSuper = true)
@TableName("attachment")
@Data
public class Attachment extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId("attachment_id")
    private Long attachmentId;
    /**
     * 原始文件名
     */
    private String originName;
    /**
     * 对象存储对象文件名
     */
    private String objectName;
    /**
     * 文件类型
     */
    private Integer type;
    /**
     * 文件大小 单位字节
     */
    private Long size;
    /**
     * 对象存储桶名称
     */
    private String bucket;




}
