package com.jiang.singlelearningdemo.monitor.event;

import com.jiang.singlelearningdemo.monitor.event.info.BaseTableEventInfo;
import org.springframework.context.ApplicationEvent;

public class InsertEvent extends ApplicationEvent {
    public <T extends BaseTableEventInfo> InsertEvent(T source) {
        super(source);
    }
    @Override
    public BaseTableEventInfo getSource() {
        return (BaseTableEventInfo) super.getSource();
    }
}
