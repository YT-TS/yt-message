package com.yt.message.admin.server.service.impl;

import io.minio.*;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName MinioService
 * @Author Ts
 * @Version 1.0
 */
@Service(value = "minio")
@Slf4j
public class MinioService {

    @Autowired
    private MinioClient minioClient;


    public void upload(String bucketName,InputStream inputStream, String fileName, Long fileSize) throws Exception {
        ObjectWriteResponse response = minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .stream(inputStream, fileSize != null ? fileSize : inputStream.available(), -1).build());

    }

    public String url(String bucketName,String objectName, Integer expiry ) throws Exception {
        return minioClient.getPresignedObjectUrl(io.minio.GetPresignedObjectUrlArgs.builder()
                .method(io.minio.http.Method.GET)
                .bucket(bucketName)
                .object(objectName)


                        .expiry(60 * 60 * 24  * expiry)
                .build());
    }

    public void remove(String bucketName,String objectName ) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build());

    }

    public void download(String bucketName, String objectName) throws Exception {
        GetObjectResponse response = minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName).build());




    }
}
