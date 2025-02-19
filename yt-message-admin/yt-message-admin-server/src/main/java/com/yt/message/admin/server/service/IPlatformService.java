package com.yt.message.admin.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yt.message.admin.server.pojo.dto.Dic;
import com.yt.message.admin.server.pojo.dto.OneLayerTreeDic;
import com.yt.message.admin.server.pojo.dto.PageResult;
import com.yt.message.admin.server.pojo.entity.Platform;
import com.yt.message.admin.server.pojo.vo.PlatformEditReqVo;
import com.yt.message.admin.server.pojo.vo.PlatformPageReqVo;
import com.yt.message.admin.server.pojo.vo.PlatformReqVo;
import com.yt.message.admin.server.pojo.vo.PlatformRspVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yt
 * @since 2024-11-26
 */
public interface IPlatformService extends IService<Platform> {

    PageResult<PlatformRspVo> page(PlatformPageReqVo reqVo);


    PlatformRspVo view(Long platformId);

    void remove(Long platformId);


    void edit(PlatformEditReqVo reqVo) throws Exception;

    void add(PlatformReqVo reqVo) throws Exception;

    List<OneLayerTreeDic<Integer,String>>  treeDic();

    List<Dic<String>> dic();

    String getPlatformName(long l);
}
