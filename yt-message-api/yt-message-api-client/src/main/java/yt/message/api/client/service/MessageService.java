package yt.message.api.client.service;


import yt.message.api.client.pojo.dto.MessageSendRsp;
import yt.message.api.client.pojo.dto.SendMessageRequest;

/**
 * @ClassName SendMessageService
 * @Author Ts
 * @Version 1.0
 */


public interface MessageService {

    MessageSendRsp sendMessage(SendMessageRequest message);




}
