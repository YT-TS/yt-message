package com.yt.message.log.filter;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import cn.hutool.core.collection.CollUtil;
import com.yt.message.log.utils.BizLogUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;

import java.util.List;

/**
 * @Author Ts
 * @Version 1.0
 *
 */

@Slf4j
public class BizAndErrorLogFilter extends Filter<ILoggingEvent> {
    @Override
    public FilterReply decide(ILoggingEvent iLoggingEvent) {
        if (iLoggingEvent.getLevel().equals(Level.ERROR)) {
            return FilterReply.ACCEPT;
        }
        List<Marker> markerList = iLoggingEvent.getMarkerList();
        if (CollUtil.isNotEmpty(markerList)){
            boolean bizLog = markerList.stream().anyMatch(marker -> marker.equals(BizLogUtils.bizMarker));
            if (bizLog){
                return FilterReply.ACCEPT;
            }
        }
        return FilterReply.DENY;
    }
}
