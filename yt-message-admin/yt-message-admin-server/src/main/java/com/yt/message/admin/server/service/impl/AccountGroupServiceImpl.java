package com.yt.message.admin.server.service.impl;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yt.message.admin.server.constant.BusinessConstant;
import com.yt.message.admin.server.constant.AccountType;
import com.yt.message.admin.server.exception.BusinessException;
import com.yt.message.admin.server.exception.IllegalRequestException;
import com.yt.message.admin.server.mapper.AccountGroupMapper;
import com.yt.message.admin.server.pojo.dto.Dic;
import com.yt.message.admin.server.pojo.dto.PageResult;
import com.yt.message.admin.server.pojo.entity.AccountGroup;
import com.yt.message.admin.server.pojo.vo.AccountGroupEditReqVo;
import com.yt.message.admin.server.pojo.vo.AccountGroupPageReqVo;
import com.yt.message.admin.server.pojo.vo.AccountGroupReqVo;
import com.yt.message.admin.server.pojo.vo.AccountGroupRspVo;
import com.yt.message.admin.server.service.IAccountGroupService;
import com.yt.message.admin.server.service.IPreparedTemplateService;
import com.yt.message.admin.server.utils.AccountCheckUtils;
import com.yt.message.admin.server.utils.ExceptionAssert;
import com.yt.message.admin.server.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 接收账户组 服务实现类
 * </p>
 *
 * @author yt
 * @since 2025-01-09
 */
@Service
public class AccountGroupServiceImpl extends ServiceImpl<AccountGroupMapper, AccountGroup> implements IAccountGroupService {
    @Autowired
    private IPreparedTemplateService preparedTemplateService;
    @Override
    public PageResult<AccountGroupRspVo> page(AccountGroupPageReqVo reqVo) {
        IPage<AccountGroup> page = new Page<>(reqVo.getPageNum(), reqVo.getPageSize());
        LambdaQueryWrapper<AccountGroup> queryWrapper = new LambdaQueryWrapper<AccountGroup>().eq(reqVo.getType() != null, AccountGroup::getType, reqVo.getType());
        this.page(page, queryWrapper);
        return PageUtils.toPageResult(page, AccountGroupRspVo.class);
    }

    @Override
    public void add(AccountGroupReqVo reqVo) throws Exception {
        AccountType accountType = AccountType.getByCode(reqVo.getType());
        ExceptionAssert.throwOnFalse(accountType != null, new IllegalRequestException(StrUtil.format("非法参数，{}", "type")));
        //格式校验
        boolean result = AccountCheckUtils.checkAccounts(accountType, reqVo.getMembers());

        ExceptionAssert.throwOnFalse(result, new BusinessException("接收账户存在格式错误"));
        AccountGroup accountGroup = new AccountGroup();
        accountGroup.setGroupName(reqVo.getGroupName());
        accountGroup.setMembers(StrUtil.join(BusinessConstant.LIST_SEPARATOR, reqVo.getMembers()));
        accountGroup.setType(reqVo.getType());
        this.save(accountGroup);

    }

    @Override
    public void remove(Long groupId) throws Exception {
        ExceptionAssert.throwOnFalse(!preparedTemplateService.existByAccountGroupId(groupId), new BusinessException("存在预发送消息模板与该账户组绑定，不能删除"));
        this.removeById(groupId);
    }

    @Override
    public void edit(AccountGroupEditReqVo reqVo) throws Exception {
        AccountType accountType = AccountType.getByCode(reqVo.getType());
        ExceptionAssert.throwOnFalse(accountType != null, new IllegalRequestException(StrUtil.format("非法参数，{}", "type")));
        //格式校验
        boolean result = AccountCheckUtils.checkAccounts(accountType, reqVo.getMembers());

        ExceptionAssert.throwOnFalse(result, new BusinessException("接收账户存在格式错误"));
        LambdaUpdateWrapper<AccountGroup> wrapper = new LambdaUpdateWrapper<AccountGroup>()
                .eq(AccountGroup::getGroupId, reqVo.getGroupId())
                .set(AccountGroup::getGroupName, reqVo.getGroupName())
                .set(AccountGroup::getMembers, StrUtil.join(BusinessConstant.LIST_SEPARATOR, reqVo.getMembers()));
        this.update(wrapper);
    }

    @Override
    public List<Dic<String>> dic() {
        LambdaQueryWrapper<AccountGroup> wrapper = new LambdaQueryWrapper<AccountGroup>().select(AccountGroup::getGroupId, AccountGroup::getGroupName);
        List<AccountGroup> list = this.list(wrapper);
        return list.stream().map(accountGroup -> new Dic<>(accountGroup.getGroupName(), accountGroup.getGroupId().toString())).collect(Collectors.toList());
    }

    @Override
    public String members(Long groupId) {
        LambdaQueryWrapper<AccountGroup> wrapper = new LambdaQueryWrapper<AccountGroup>().select(AccountGroup::getMembers)
                .eq(AccountGroup::getGroupId, groupId);
        return this.getOne(wrapper).getMembers();
    }

}
