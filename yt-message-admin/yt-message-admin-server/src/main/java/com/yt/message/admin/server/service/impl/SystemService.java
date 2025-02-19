package com.yt.message.admin.server.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.yt.message.admin.server.pojo.vo.ErrorLogPageReqVo;
import com.yt.message.admin.server.service.ISystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @ClassName SystemService
 * @Author Ts
 * @Version 1.0
 */
@Service
public class SystemService implements ISystemService {

    @Value("${graylog.errorStreamId}")
    private String errorStreamId;
    private static final String postBody = """
            {
                "streams":["{}"],
                "fields": [
                    "timestamp",
                    "message",
                    "full_message"
                 ],
                "timerange": {
                    "from": "{}",
                    "to": "{}",
                    "type": "absolute"
                 },
                "from": {},
                "size":{}
            }
            """;
    private static final String dayStart = "T00:00:00.000+08:00";
    private static final String dayEnd = "T23:59:59.999+08:00";
    @Autowired
    private GrayLogRequestService grayLogRequestService;
    @Override
    public String errorLogPage(ErrorLogPageReqVo reqVo) throws IOException {
        String date = StrUtil.isNotBlank(reqVo.getDate())? reqVo.getDate() : DateUtil.today();
        String start = date + dayStart;
        String end = date + dayEnd;
        Long from = (reqVo.getPageNum() - 1) * reqVo.getPageSize();
        String body = StrUtil.format(postBody,  errorStreamId,start,end ,from, reqVo.getPageSize());
        return grayLogRequestService.post(body);
    }
}
