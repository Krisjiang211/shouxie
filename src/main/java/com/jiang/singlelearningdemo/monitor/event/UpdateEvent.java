package com.jiang.singlelearningdemo.monitor.event;

import com.jiang.singlelearningdemo.monitor.event.info.BaseTableEventInfo;
import org.springframework.context.ApplicationEvent;

public class UpdateEvent extends ApplicationEvent {
    public <T extends BaseTableEventInfo> UpdateEvent(T source) {
        super(source);
    }
    @Override
    public BaseTableEventInfo getSource() {
        return (BaseTableEventInfo) super.getSource();
    }
}
