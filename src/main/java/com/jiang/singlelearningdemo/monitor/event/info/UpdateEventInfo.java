package com.jiang.singlelearningdemo.monitor.event.info;

public class UpdateEventInfo extends BaseTableEventInfo{

    public UpdateEventInfo(String tableName){
        this.setEventType("UPDATE");
        this.setTableName(tableName);
    }
}
