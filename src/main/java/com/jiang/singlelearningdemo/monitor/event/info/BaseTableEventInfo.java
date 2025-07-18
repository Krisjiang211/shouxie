package com.jiang.singlelearningdemo.monitor.event.info;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BaseTableEventInfo {
    private String tableName;
    private String eventType;
    private long timeStamp=System.currentTimeMillis();

}
