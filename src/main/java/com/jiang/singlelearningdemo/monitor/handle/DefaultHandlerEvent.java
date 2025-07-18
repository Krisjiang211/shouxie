package com.jiang.singlelearningdemo.monitor.handle;

import com.jiang.singlelearningdemo.monitor.event.DeleteEvent;
import com.jiang.singlelearningdemo.monitor.event.InsertEvent;
import com.jiang.singlelearningdemo.monitor.event.SelectEvent;
import com.jiang.singlelearningdemo.monitor.event.UpdateEvent;
import org.springframework.stereotype.Component;

@Component
public class DefaultHandlerEvent implements HandleEvent {
    @Override
    public void handleSelect(SelectEvent event) {

    }

    @Override
    public void handleUpdate(UpdateEvent event) {

    }

    @Override
    public void handleDelete(DeleteEvent event) {

    }

    @Override
    public void handleInsert(InsertEvent event) {

    }
}
