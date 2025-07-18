package com.jiang.singlelearningdemo.monitor.event.info;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

public class SelectEventInfo extends BaseTableEventInfo{

    public SelectEventInfo(String tableName){
        this.setEventType("SELECT");
        this.setTableName(tableName);
    }
}
