package com.yt.message.admin.server.pojo.entity;

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
@TableName("account_group")
@Data
public class AccountGroup extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId("group_id")
    private Long groupId;
    /**
     * 账户组名称
     */
    private String groupName;
    /**
     * 账户，每个账户用,隔开
     */
    private String members;
    /**
     * 账户类型
     */
    private Integer type;


}
