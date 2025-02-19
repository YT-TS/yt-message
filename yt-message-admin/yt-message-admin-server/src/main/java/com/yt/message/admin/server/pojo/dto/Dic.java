package com.yt.message.admin.server.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName Dic
 * @Description 字典
 * @Author Ts
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dic<T> {

    private String label;

    private T value;
}
