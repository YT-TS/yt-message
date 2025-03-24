package com.yt.message.admin.server.service.impl;


import cn.hutool.core.io.file.FileNameUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yt.message.admin.server.constant.AttachmentType;
import com.yt.message.admin.server.exception.IllegalRequestException;
import com.yt.message.admin.server.mapper.AttachmentMapper;
import com.yt.message.admin.server.pojo.dto.PageResult;
import com.yt.message.admin.server.pojo.entity.Attachment;
import com.yt.message.admin.server.pojo.vo.AttachmentPageReqVo;
import com.yt.message.admin.server.pojo.vo.AttachmentRspVo;
import com.yt.message.admin.server.pojo.vo.AttachmentUrlVo;
import com.yt.message.admin.server.service.IAttachmentService;
import com.yt.message.admin.server.utils.PageUtils;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yt
 * @since 2025-02-10
 */
@Service
public class AttachmentServiceImpl extends ServiceImpl<AttachmentMapper, Attachment> implements IAttachmentService {

    @Value("${minio.attachment-bucket}")
    private String attachmentBucket;
    @Autowired
    private MinioService minioService;

    @Override
    public PageResult<AttachmentRspVo> page(AttachmentPageReqVo reqVo) {
        IPage<Attachment> page = new Page<>(reqVo.getPageNum(), reqVo.getPageSize());
        LambdaQueryWrapper<Attachment> queryWrapper = new LambdaQueryWrapper<Attachment>()
                .eq(reqVo.getType() != null, Attachment::getType, reqVo.getType());
        this.page(page, queryWrapper);
        return PageUtils.toPageResult(page, AttachmentRspVo.class);
    }

    @Override
    @Transactional
    public void upload(MultipartFile file,Integer type) throws Exception {
        Optional.ofNullable(AttachmentType.getByCode(type))
                .orElseThrow(() -> new IllegalRequestException("非法参数: type"));

        long size = file.getSize();
        String originalFilename = file.getOriginalFilename();
        Attachment attachment = new Attachment();
        long id = IdWorker.getId();
        attachment.setAttachmentId(id);
        attachment.setSize(size);
        attachment.setOriginName(originalFilename);
        attachment.setBucket(attachmentBucket);
        attachment.setType(type);
        //文件扩展名
        String extName = FileNameUtil.extName(originalFilename);
        String objectName = id + "." + extName;
        attachment.setObjectName(objectName);
        baseMapper.insert(attachment);

        minioService.upload(attachmentBucket,file.getInputStream(),objectName , size);

    }

    @Override
    public AttachmentUrlVo url(Long attachmentId, Integer expiry ) throws Exception {
        Attachment attachment = this.getById(attachmentId);
        Optional.ofNullable(attachment).orElseThrow(() -> new IllegalRequestException("附件不存在: " + attachmentId));
        String bucket = attachment.getBucket();
        String objectName = attachment.getObjectName();
        String url = minioService.url(bucket, objectName,  expiry );

        return new AttachmentUrlVo(url);
    }

    @Override
    @Transactional
    public void remove(Long attachmentId) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        Attachment attachment = this.getById(attachmentId);
        Optional.ofNullable(attachment).orElseThrow(() -> new IllegalRequestException("附件不存在: " + attachmentId));
        this.removeById(attachmentId);

        minioService.remove(attachment.getBucket(), attachment.getObjectName());
    }

    @Override
    public void download(Long attachmentId) throws Exception {
        Attachment attachment = this.getById(attachmentId);
        Optional.ofNullable(attachment).orElseThrow(() -> new IllegalRequestException("附件不存在: " + attachmentId));
        minioService.download(attachment.getBucket(), attachment.getObjectName());


    }
}
