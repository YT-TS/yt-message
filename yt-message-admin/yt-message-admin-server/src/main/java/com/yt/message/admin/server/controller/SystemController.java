package com.yt.message.admin.server.controller;

import com.yt.message.admin.server.pojo.vo.ErrorLogPageReqVo;
import com.yt.message.admin.server.service.ISystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName SystemController
 * @Author Ts
 * @Version 1.0
 */
@RestController
@RequestMapping("/system")
public class SystemController {
    @Autowired
    private ISystemService systemService;
    @PostMapping("/page")
    public String errorLogPage(@RequestBody ErrorLogPageReqVo reqVo) throws Exception{
        return systemService.errorLogPage(reqVo);
    }


}
