package com.yt.message.handler.pojo.dto;

import com.yt.message.common.enums.ResultCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName MessageSendResult
 * @Author Ts
 * @Version 1.0
 */

@AllArgsConstructor
@Getter
public class MessageSendResult {
    private int code;
    private String msg;


    public static MessageSendResult success(String msg){
        return new MessageSendResult(ResultCodeEnum.SUCCESS.getCode(),msg);
    }
    public static MessageSendResult success(){
        return success("发送成功");
    }

    public static MessageSendResult fail(String msg){
        return new MessageSendResult(ResultCodeEnum.FAIL.getCode(), msg);
    }
    public static MessageSendResult fail(){
        return fail("发送失败");
    }







}
