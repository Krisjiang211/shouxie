package com.jiang.singlelearningdemo.monitor.event.info;

import com.jiang.singlelearningdemo.monitor.event.info.BaseTableEventInfo;

public class DeleteEventInfo extends BaseTableEventInfo {
    public DeleteEventInfo(String tableName) {
        this.setEventType("DELETE");
        this.setTableName(tableName);
    }
}
