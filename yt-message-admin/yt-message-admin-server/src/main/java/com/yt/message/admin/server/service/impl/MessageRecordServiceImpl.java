package com.yt.message.admin.server.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.yt.message.admin.server.pojo.vo.MessageRecordPageReqVo;
import com.yt.message.admin.server.service.IMessageRecordService;
import com.yt.message.admin.server.utils.ExceptionAssert;
import com.yt.message.common.enums.MessageTrack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @ClassName MessageRecordServiceImpl
 * @Author Ts
 * @Version 1.0
 */
@Service
@Slf4j
public class MessageRecordServiceImpl implements IMessageRecordService {
   @Autowired
   private GrayLogRequestService grayLogRequestService;

    @Value("${graylog.bizStreamId}")
    private String bizStreamId;
    private static final String postBody = """
            {
                "query": "{}",
                "streams":["{}"],
                "fields": [
                    "biz_id",
                    "hand_timestamp",
                    "step",
                    "receive_account",
                    "template_id",
                    "note"
                ],
                "timerange": {
                    "type": "keyword",
                    "keyword": "Last fifteen days"
                },
                "sort": "hand_timestamp",
                "sort_order": "desc",
                "from": {},
                "size":{}
            }
            """;
    private static final String trackPostBody = """
            {
                "query": "{}",
                "streams":["{}"],
                "fields": [
                    "biz_id",
                    "hand_timestamp",
                    "step",
                    "receive_account",
                    "template_id",
                    "note"
                ],
                "timerange": {
                    "type": "keyword",
                    "keyword": "Last fifteen days"
                },
                "sort": "hand_timestamp",
                "sort_order": "asc",
                "from": {},
                "size":{}
            }
            """;
    private static final String queryString = "(step:" + MessageTrack.ISSUE_SUCCESS.getCode() + " OR step:\\\\" + MessageTrack.ISSUE_FAIL.getCode() + ")";

    @Override
    public String page(MessageRecordPageReqVo reqVo) throws Exception {
        StringBuilder queryString = new StringBuilder(MessageRecordServiceImpl.queryString);
        if (reqVo.getTemplateId() != null) {
            queryString.append(" AND template_id:").append(reqVo.getTemplateId());
        }
        if (StrUtil.isNotBlank(reqVo.getReceiveAccount())) {
            queryString.append(" AND receive_account:").append(reqVo.getReceiveAccount());
        }
        if (reqVo.getMessageId() != null) {
            queryString.append(" AND biz_id:").append(reqVo.getMessageId());
        }
        if (reqVo.getEndTimeStamp() != null && reqVo.getStartTimeStamp() != null) {
            //时间范围校验
            ExceptionAssert.throwOnFalse(reqVo.getEndTimeStamp() >= reqVo.getStartTimeStamp(), new Exception("结束时间不能小于开始时间"));
        }
        if (reqVo.getStartTimeStamp() != null) {
            //15天前时间戳 搜索时间不能超过15天
            DateTime now = DateUtil.date();
            long fifteenDaysAgo = DateUtil.offsetDay(now, -15).toTimestamp().getTime();
            if (reqVo.getStartTimeStamp() < fifteenDaysAgo) {
                reqVo.setStartTimeStamp(fifteenDaysAgo);
            }
            queryString.append(" AND hand_timestamp:>=").append(reqVo.getStartTimeStamp());
        }
        if (reqVo.getEndTimeStamp() != null) {
            queryString.append(" AND hand_timestamp:<=").append(reqVo.getEndTimeStamp());
        }

        Long from = (reqVo.getPageNum() - 1) * reqVo.getPageSize();
        String body = StrUtil.format(postBody, queryString.toString(), bizStreamId, from, reqVo.getPageSize());
        return grayLogRequestService.post(body);
    }

    @Override
    public String track(Long messageId) throws IOException {
        String query = "biz_id:" + messageId;
        String body = StrUtil.format(trackPostBody, query, bizStreamId, 0, 10);
        return grayLogRequestService.post(body);
    }


}
