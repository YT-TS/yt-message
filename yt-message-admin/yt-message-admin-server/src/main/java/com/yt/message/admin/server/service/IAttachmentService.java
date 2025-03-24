package com.yt.message.admin.server.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yt.message.admin.server.pojo.dto.PageResult;
import com.yt.message.admin.server.pojo.entity.Attachment;
import com.yt.message.admin.server.pojo.vo.AttachmentPageReqVo;
import com.yt.message.admin.server.pojo.vo.AttachmentRspVo;
import com.yt.message.admin.server.pojo.vo.AttachmentUrlVo;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yt
 * @since 2025-02-10
 */
public interface IAttachmentService extends IService<Attachment> {

    PageResult<AttachmentRspVo> page(AttachmentPageReqVo reqVo);

    void upload(MultipartFile file,Integer type) throws Exception;

    AttachmentUrlVo url(Long attachmentId, Integer expiry ) throws Exception;

    void remove(Long attachmentId) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    void download(Long attachmentId) throws Exception;
}
