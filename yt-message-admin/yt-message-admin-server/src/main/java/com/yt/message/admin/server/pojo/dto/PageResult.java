package com.yt.message.admin.server.pojo.dto;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {
    // 当前页码
    private Long pageNum;
    // 每页数量
    private Long pageSize;
    //总共多少页
    private Long pageCount;
    //总数据量
    private Long totalCount;
    private List<T> records;
}
