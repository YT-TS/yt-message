package com.yt.message.admin.server.service.impl;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @ClassName GrayLogRequestService
 * @Author Ts
 * @Version 1.0
 */
@Service
public class GrayLogRequestService {
    @Value("${graylog.ip}")
    private String ip;
    @Value("${graylog.port}")
    private String port;


    @Value("${graylog.authorization}")
    private String authorization;
    @Autowired
    private CloseableHttpClient httpClient;

    String post(String postBody) throws IOException {
        HttpPost httpPost = new HttpPost("http://" + ip + ":" + port + "/api/search/messages");
        httpPost.setHeader(HTTP.CONTENT_TYPE, "application/json");
        httpPost.setHeader("X-Requested-By", "yt");
        httpPost.setHeader("Authorization", authorization);
        httpPost.setEntity(new StringEntity(postBody));
        httpPost.setHeader("Accept", "application/json");

        return httpClient.execute(httpPost, new BasicHttpClientResponseHandler());
    }


}
