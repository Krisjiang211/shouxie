package com.jiang.singlelearningdemo.objPool;

import com.jiang.singlelearningdemo.objPool.defaultImpl.DefaultRejectStrategy;
import com.jiang.singlelearningdemo.objPool.utils.GiveBackStrategy;
import com.jiang.singlelearningdemo.objPool.utils.ObjPoolUtil;
import com.jiang.singlelearningdemo.objPool.utils.RejectStrategy;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public abstract class DataStructure<T> {

    protected int nums;//容量

    protected Collection<? extends ObjPool.Obj<T>> container;

    protected RejectStrategy<T> rejectStrategy;//拒绝策略: 当申请对象遭拒绝后的操作
    protected GiveBackStrategy<T> giveBackStrategy;//归还策略: 当归还对象的时候需要如何重置对象.

    protected ObjPoolUtil<T> util;//其他的一些拓展

    protected Constructor<T> constructor;//原始对象的构造器
    protected Object[] originObjValues;//原始对象的参数值

    public DataStructure(int nums,
                         Constructor<T> constructor,
                         Object[] originObjValues,
                         RejectStrategy<T> rejectStrategy,
                         GiveBackStrategy<T> giveBackStrategy) {
        this.rejectStrategy = Objects.requireNonNullElseGet(rejectStrategy, DefaultRejectStrategy::new);
        this.giveBackStrategy = giveBackStrategy;
        this.constructor = constructor;
        this.originObjValues = originObjValues;
        if (nums <= 0){
            this.nums=1;
        }else {
            this.nums=nums;
        }
    }

    //初始化对象池的时候添加对象
    protected abstract void offer(ObjPool.Obj<T> obj);

    //尝试一次获取元素
    protected abstract ObjPool.Obj<T> tryApply();

    //从对象池申请一个对象(阻塞, 直到申请到为止)
    public abstract ObjPool.Obj<T> apply();


    //从对象池申请一个对象(阻塞, 直到申请到或者超时为止)
    public abstract ObjPool.Obj<T> apply(long timeout, TimeUnit timeUnit);


    //归还一个对象
    public abstract boolean giveBack(ObjPool.Obj<T> obj);

    //总容量
    public abstract int getCapacity();

    //当前对象池中可用的容量
    public abstract int getAvailableCapacity();

    protected void setConstructor(Constructor<T> constructor){
        this.constructor=constructor;
    }
    protected void setOriginObjValues(Object[] originObjValues){
        this.originObjValues=originObjValues;
    }


}
