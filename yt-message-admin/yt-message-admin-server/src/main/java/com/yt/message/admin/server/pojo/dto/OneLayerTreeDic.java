package com.yt.message.admin.server.pojo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @ClassName OneLayerTreeDic
 * @Author Ts
 * @Version 1.0
 */
@Getter
@Setter
public class OneLayerTreeDic<T,C> extends Dic<T>{

    private List<Dic<C>> children;


    public OneLayerTreeDic(String label, T value, List<Dic<C>> children) {
        super(label, value);
        this.children = children;
    }

    public OneLayerTreeDic<T,C> setChildren(List<Dic<C>> children) {
        this.children = children;
        return this;
    }
}
