package com.yt.message.admin.server.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author yt
 * @since 2024-11-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Record extends  BaseEntity implements Serializable  {

    private static final long serialVersionUID = 1L;

    /**
     * 记录id
     */
    @TableId("record_id")
    private Long recordId;

    /**
     * 模板id
     */
    private Long templateId;

    /**
     * 发送时间
     */
    private LocalDateTime time;

    /**
     * 下发账户
     */
    private String receiveAccount;

    /**
     * 发送结果
     */
    private Byte result;

    private String note;

}
