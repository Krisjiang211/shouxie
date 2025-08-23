package com.jiang.singlelearningdemo.objPool.defaultImpl;

import com.jiang.singlelearningdemo.objPool.ObjPool;
import com.jiang.singlelearningdemo.objPool.domain.NeedNewFlag;
import com.jiang.singlelearningdemo.objPool.utils.RejectStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;

public class DefaultRejectStrategy<T> implements RejectStrategy<T> {

    private Logger log = LoggerFactory.getLogger(DefaultRejectStrategy.class);

    @Override
    public ObjPool.Obj<T> reject(NeedNewFlag flag, Constructor<T> constructor, Object[] args) {
        flag.iNeed();
        log.info("触发了一次(默认)拒绝策略......");
        return null;
    }
}
