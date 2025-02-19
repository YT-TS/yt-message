package com.yt.message.admin.server.controller;

import com.yt.message.admin.server.pojo.vo.MessageRecordPageReqVo;
import com.yt.message.admin.server.service.IMessageRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName MessageRecordController
 * @Author Ts
 * @Version 1.0
 */
@RestController
@RequestMapping("/record")
public class MessageRecordController {
    @Autowired
    private IMessageRecordService messageRecordService;
    @PostMapping("/page")
    public String page(@RequestBody MessageRecordPageReqVo reqVo) throws Exception {
        return messageRecordService.page(reqVo);
    }
    @GetMapping("/track/{messageId}")
    public String track(@PathVariable("messageId") Long messageId) throws Exception {
        return messageRecordService.track(messageId);
    }





}
