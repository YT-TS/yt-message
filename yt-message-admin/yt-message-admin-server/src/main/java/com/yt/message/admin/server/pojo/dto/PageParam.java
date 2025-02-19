package com.yt.message.admin.server.pojo.dto;

import lombok.Data;

@Data
public class PageParam {

    private Long pageNum = 1L;
    private Long pageSize = 10L;
}
