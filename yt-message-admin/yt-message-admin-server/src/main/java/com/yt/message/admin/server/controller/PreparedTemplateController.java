package com.yt.message.admin.server.controller;


import com.yt.message.admin.server.pojo.dto.PageResult;
import com.yt.message.admin.server.pojo.vo.PreparedTemplatePageReqVo;
import com.yt.message.admin.server.pojo.vo.PreparedTemplateReqVo;
import com.yt.message.admin.server.pojo.vo.PreparedTemplateRspVo;
import com.yt.message.admin.server.pojo.vo.validation.PreparedTemplateEditReqVo;
import com.yt.message.admin.server.service.IPreparedTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author yt
 * @since 2025-01-09
 */
@RestController
@RequestMapping("/preparedTemplate")
public class PreparedTemplateController {

    @Autowired
    private IPreparedTemplateService preparedTemplateService    ;
    @PostMapping("/page")
    public PageResult<PreparedTemplateRspVo> page(@RequestBody PreparedTemplatePageReqVo reqVo) {
        return preparedTemplateService.page(reqVo);
    }

    @PostMapping
    public void add(@RequestBody @Validated PreparedTemplateReqVo reqVo) throws Exception {
        preparedTemplateService.add(reqVo);
    }
    @DeleteMapping("/{templateId}")
    public void remove( @PathVariable("templateId") Long templateId) throws Exception {
        preparedTemplateService.remove(templateId);
    }
    @PutMapping
    public void edit(@RequestBody @Validated PreparedTemplateEditReqVo reqVo) throws Exception {
        preparedTemplateService.edit(reqVo);
    }
    @PostMapping("/send/{preparedTemplateId}")
    public void send(@PathVariable("preparedTemplateId") Long preparedTemplateId) throws Exception {
        preparedTemplateService.send(preparedTemplateId);
    }



}
