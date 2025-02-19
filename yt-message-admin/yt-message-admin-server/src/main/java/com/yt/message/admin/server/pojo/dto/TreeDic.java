package com.yt.message.admin.server.pojo.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @ClassName TreeDic
 * @Author Ts
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
public class TreeDic<T> extends Dic<T> {

    private List<TreeDic<T>> children;


    public TreeDic(String label, T value, List<TreeDic<T>> children) {
        super(label, value);
        this.children = children;
    }
    public TreeDic(String label, T value) {
        super(label, value);
    }

}
