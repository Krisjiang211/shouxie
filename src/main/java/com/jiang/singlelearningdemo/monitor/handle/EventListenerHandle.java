package com.jiang.singlelearningdemo.monitor.handle;

import com.jiang.singlelearningdemo.monitor.event.DeleteEvent;
import com.jiang.singlelearningdemo.monitor.event.InsertEvent;
import com.jiang.singlelearningdemo.monitor.event.SelectEvent;
import com.jiang.singlelearningdemo.monitor.event.UpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
public class EventListenerHandle {

    @Autowired
    private HandleEvent eventHandler;

    @EventListener
    public void handleSelect(SelectEvent event){
        eventHandler.handleSelect(event);
    }
    @EventListener
    public void handleInsert(InsertEvent event){
        eventHandler.handleInsert(event);
    }
    @EventListener
    public void handleUpdate(UpdateEvent event){
        eventHandler.handleUpdate(event);
    }
    @EventListener
    public void handleDelete(DeleteEvent event) {
        eventHandler.handleDelete(event);
    }

}
