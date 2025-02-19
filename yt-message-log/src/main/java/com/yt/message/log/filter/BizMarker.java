package com.yt.message.log.filter;

import org.slf4j.Marker;

import java.util.Iterator;

/**
 * @ClassName BizMarker
 * @Description 表示业务日志
 * @Author Ts
 * @Version 1.0
 */

public class BizMarker implements Marker {
    @Override
    public String getName() {
        return "Biz";
    }

    @Override
    public void add(Marker reference) {

    }

    @Override
    public boolean remove(Marker reference) {
        return false;
    }

    @Override
    public boolean hasChildren() {
        return false;
    }

    @Override
    public boolean hasReferences() {
        return false;
    }

    @Override
    public Iterator<Marker> iterator() {
        return null;
    }

    @Override
    public boolean contains(Marker other) {
        return false;
    }

    @Override
    public boolean contains(String name) {
        return false;
    }
}
