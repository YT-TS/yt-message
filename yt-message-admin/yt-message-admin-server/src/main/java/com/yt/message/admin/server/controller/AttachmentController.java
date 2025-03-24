package com.yt.message.admin.server.controller;

import com.yt.message.admin.server.pojo.dto.PageResult;
import com.yt.message.admin.server.pojo.vo.AttachmentPageReqVo;
import com.yt.message.admin.server.pojo.vo.AttachmentRspVo;
import com.yt.message.admin.server.pojo.vo.AttachmentUrlVo;
import com.yt.message.admin.server.service.IAttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName FILETransmissionController
 * @Author Ts
 * @Version 1.0
 */
@RestController
@RequestMapping("/attachment")
public class AttachmentController {
    @Autowired
    private IAttachmentService attachmentService;


    @PostMapping("/page")
    public PageResult<AttachmentRspVo> page(@RequestBody AttachmentPageReqVo reqVo) {
        return attachmentService.page(reqVo);
    }

    @PostMapping("/upload")
    public void upload( @RequestParam("file") MultipartFile file,@RequestParam("type") Integer type) throws Exception {
        attachmentService.upload(file,type);
    }
    @GetMapping("/url/{attachmentId}/{expiry}")
    public AttachmentUrlVo url(@PathVariable("attachmentId") Long attachmentId,@PathVariable("expiry") Integer expiry ) throws Exception {
        return attachmentService.url(attachmentId,expiry);
    }
    @DeleteMapping("/{attachmentId}")
    public void remove(@PathVariable("attachmentId") Long attachmentId) throws Exception {
        attachmentService.remove(attachmentId);
    }

//    @GetMapping("/download/{attachmentId}")
//    public void download(@PathVariable("attachmentId") Long attachmentId) throws Exception {
//        attachmentService.download(attachmentId);
//    }

}
