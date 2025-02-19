package com.yt.message.admin.server.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yt.message.admin.server.pojo.dto.Dic;
import com.yt.message.admin.server.pojo.dto.PageResult;
import com.yt.message.admin.server.pojo.entity.AccountGroup;
import com.yt.message.admin.server.pojo.vo.AccountGroupEditReqVo;
import com.yt.message.admin.server.pojo.vo.AccountGroupPageReqVo;
import com.yt.message.admin.server.pojo.vo.AccountGroupReqVo;
import com.yt.message.admin.server.pojo.vo.AccountGroupRspVo;

import java.util.List;

/**
 * <p>
 * 接收账户组 服务类
 * </p>
 *
 * @author yt
 * @since 2025-01-09
 */
public interface IAccountGroupService extends IService<AccountGroup> {


    PageResult<AccountGroupRspVo> page(AccountGroupPageReqVo reqVo);

    void add(AccountGroupReqVo reqVo) throws Exception;

    void remove(Long groupId) throws Exception;

    void edit(AccountGroupEditReqVo reqVo) throws Exception;

    List<Dic<String>> dic();

    String members(Long groupId);
}
