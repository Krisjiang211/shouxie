package com.jiang.singlelearningdemo.monitor.event.info;

public class InsertEventInfo extends BaseTableEventInfo{
    public InsertEventInfo(String tableName){
        this.setEventType("INSERT");
        this.setTableName(tableName);
    }
}
