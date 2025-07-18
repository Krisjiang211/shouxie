package com.jiang.singlelearningdemo.monitor.handle;

import com.jiang.singlelearningdemo.monitor.event.DeleteEvent;
import com.jiang.singlelearningdemo.monitor.event.InsertEvent;
import com.jiang.singlelearningdemo.monitor.event.SelectEvent;
import com.jiang.singlelearningdemo.monitor.event.UpdateEvent;

public interface HandleEvent {
    void handleSelect(SelectEvent event);
    void handleUpdate(UpdateEvent event);
    void handleDelete(DeleteEvent event);
    void handleInsert(InsertEvent event);
}
