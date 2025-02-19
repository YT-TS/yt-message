package yt.message.api.client.pojo.dto;

import com.yt.message.common.enums.ResultCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName MessageSendRsp
 * @Author Ts
 * @Version 1.0
 */

@AllArgsConstructor
@Getter
public class MessageSendRsp implements java.io.Serializable{
    private int code;
    private String msg;


    public static MessageSendRsp success(String msg){
        return new MessageSendRsp(ResultCodeEnum.SUCCESS.getCode(),msg);
    }
    public static MessageSendRsp success(){
        return success("发送成功");
    }

    public static MessageSendRsp fail(String msg){
        return new MessageSendRsp(ResultCodeEnum.FAIL.getCode(), msg);
    }
    public static MessageSendRsp fail(){
        return fail("发送失败");
    }






}
