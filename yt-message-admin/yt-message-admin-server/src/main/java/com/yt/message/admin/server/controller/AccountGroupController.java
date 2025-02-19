package com.yt.message.admin.server.controller;

import com.yt.message.admin.server.pojo.dto.PageResult;
import com.yt.message.admin.server.pojo.vo.AccountGroupEditReqVo;
import com.yt.message.admin.server.pojo.vo.AccountGroupPageReqVo;
import com.yt.message.admin.server.pojo.vo.AccountGroupReqVo;
import com.yt.message.admin.server.pojo.vo.AccountGroupRspVo;
import com.yt.message.admin.server.service.IAccountGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 接收账户组 前端控制器
 * </p>
 *
 * @author yt
 * @since 2025-01-09
 */
@RestController
@RequestMapping("/account-group")
public class AccountGroupController {

    @Autowired
    private IAccountGroupService accountGroupService;
    @PostMapping("/page")
    public PageResult<AccountGroupRspVo> page(@RequestBody AccountGroupPageReqVo reqVo) {
        return accountGroupService.page(reqVo);
    }

    @PostMapping
    public void add(@RequestBody @Validated AccountGroupReqVo reqVo) throws Exception {
        accountGroupService.add(reqVo);
    }
    @DeleteMapping("/{groupId}")
    public void remove( @PathVariable("groupId") Long groupId) throws Exception {
        accountGroupService.remove(groupId);
    }
    @PutMapping
    public void edit(@RequestBody @Validated AccountGroupEditReqVo reqVo) throws Exception {
        accountGroupService.edit(reqVo);
    }
    @GetMapping("/members/{groupId}")
    public String members(@PathVariable("groupId") Long groupId){
        return accountGroupService.members(groupId);
    }


}
