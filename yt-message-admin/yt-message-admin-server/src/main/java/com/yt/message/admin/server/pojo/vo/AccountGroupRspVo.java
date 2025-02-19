package com.yt.message.admin.server.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName AccountGroupRspVo
 * @Author Ts
 * @Version 1.0
 */
@Data
public class AccountGroupRspVo {
    /**
     * 组id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;




}
