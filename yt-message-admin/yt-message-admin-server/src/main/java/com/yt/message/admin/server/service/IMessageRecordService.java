package com.yt.message.admin.server.service;

import com.yt.message.admin.server.pojo.vo.MessageRecordPageReqVo;

import java.io.IOException;

/**
 * @ClassName IMessageRecordService
 * @Author Ts
 * @Version 1.0
 */

public interface IMessageRecordService {


    String page(MessageRecordPageReqVo reqVo) throws Exception;

    String track(Long messageId) throws IOException;
}
