package com.yt.message.admin.server.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName MinioConfiguration
 * @Description Minio配置
 * @Author Ts
 * @Version 1.0
 */
@Configuration
public class MinioConfiguration {
    @Value("${minio.endpoint}")
    private  String endpoint;
    @Value("${minio.accessKey}")
    private  String accessKey;
    @Value("${minio.secretKey}")
    private  String secretKey;




    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .credentials(accessKey, secretKey)
                .endpoint(endpoint)
                .build();
    }

}
