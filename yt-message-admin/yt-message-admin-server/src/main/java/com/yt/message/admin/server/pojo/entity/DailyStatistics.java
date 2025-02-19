package com.yt.message.admin.server.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author yt
 * @since 2025-02-10
 */
@EqualsAndHashCode(callSuper = true)
@TableName("daily_statistics")
@Data
public class DailyStatistics extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    private Date date;

    private Long numberOfSuccess;

    private Long numberOfFail;


}
