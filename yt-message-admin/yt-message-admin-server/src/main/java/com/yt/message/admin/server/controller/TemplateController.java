package com.yt.message.admin.server.controller;


import com.yt.message.admin.server.pojo.dto.PageResult;

import com.yt.message.admin.server.pojo.vo.*;
import com.yt.message.admin.server.service.ITemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 消息模板 前端控制器
 * </p>
 *
 * @author yt
 * @since 2024-11-26
 */
@RestController
@RequestMapping("/template")
public class TemplateController {


    @Autowired
    private ITemplateService templateService;

    @PostMapping("/page")
    public PageResult<TemplateRspVo> page(@RequestBody TemplatePageReqVo reqVo) {
        return templateService.page(reqVo);
    }
    @PutMapping
    public void edit(@RequestBody @Validated TemplateEditReqVo reqVo) throws Exception {
        templateService.edit(reqVo);
    }
    @PostMapping
    public void add(@RequestBody @Validated TemplateReqVo reqVo) throws Exception {
        templateService.add(reqVo);
    }
    @DeleteMapping("/{templateId}")
    public void remove(@PathVariable("templateId") Long templateId) {
        templateService.remove(templateId);
    }
    @PostMapping("/send")
    public void send(@RequestBody @Validated MessageSendVo reqVo) throws Exception {
        templateService.send(reqVo);
    }

}
