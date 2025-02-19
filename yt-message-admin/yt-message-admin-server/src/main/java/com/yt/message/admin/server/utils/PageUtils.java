package com.yt.message.admin.server.utils;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yt.message.admin.server.pojo.dto.PageResult;

public class PageUtils {

    public static <T> PageResult<T> toPageResult(IPage<T> page) {
        PageResult<T> respParam = new PageResult<>();
        respParam.setPageNum(page.getCurrent());
        respParam.setPageSize(page.getSize());
        respParam.setPageCount(page.getPages());
        respParam.setTotalCount(page.getTotal());
        respParam.setRecords(page.getRecords());
        return respParam;
    }
    public static <T> PageResult<T> toPageResult(IPage<?> page, Class<T> target) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setPageNum(page.getCurrent());
        pageResult.setPageSize(page.getSize());
        pageResult.setPageCount(page.getPages());
        pageResult.setTotalCount(page.getTotal());
        pageResult.setRecords(BeanUtil.copyToList(page.getRecords(), target));
        return pageResult;
    }


}