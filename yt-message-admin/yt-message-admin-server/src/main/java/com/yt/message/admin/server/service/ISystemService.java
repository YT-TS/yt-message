package com.yt.message.admin.server.service;

import com.yt.message.admin.server.pojo.vo.ErrorLogPageReqVo;

import java.io.IOException;

/**
 * @ClassName ISystemService
 * @Author Ts
 * @Version 1.0
 */

public interface ISystemService {

    String errorLogPage(ErrorLogPageReqVo reqVo) throws IOException;

}
