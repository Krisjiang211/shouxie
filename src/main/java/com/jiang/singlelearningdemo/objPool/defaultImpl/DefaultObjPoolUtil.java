package com.jiang.singlelearningdemo.objPool.defaultImpl;

import com.jiang.singlelearningdemo.objPool.utils.ObjPoolUtil;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DefaultObjPoolUtil<T> implements ObjPoolUtil<T> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public String dateFormat(LocalDateTime dateTime) {
        return formatter.format(dateTime);
    }

}
