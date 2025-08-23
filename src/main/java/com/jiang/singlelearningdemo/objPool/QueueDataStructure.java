package com.jiang.singlelearningdemo.objPool;

import com.jiang.singlelearningdemo.objPool.domain.NeedNewFlag;
import com.jiang.singlelearningdemo.objPool.utils.GiveBackStrategy;
import com.jiang.singlelearningdemo.objPool.utils.RejectStrategy;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class QueueDataStructure<T> extends DataStructure<T> {

    protected final ArrayBlockingQueue<ObjPool.Obj<T>> queue;

    protected QueueDataStructure(int nums,
                              Constructor<T> constructor,
                              Object[] originObjValues,
                              RejectStrategy<T> rejectStrategy,
                              GiveBackStrategy<T> giveBackStrategy) {
        super(nums, constructor, originObjValues, rejectStrategy, giveBackStrategy);
        queue = new ArrayBlockingQueue<>(nums);
        container=queue;
    }

    @Override
    protected void offer(ObjPool.Obj<T> obj) {
        queue.offer(obj);
    }

    public static <T> QueueDataStructure<T> newDefault(int nums, GiveBackStrategy<T> giveBackStrategy){
        return new QueueDataStructure<>(nums,null,null,null,giveBackStrategy);
    }

    @Override
    public ObjPool.Obj<T> apply() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ObjPool.Obj<T> apply(long timeout, TimeUnit unit) {
        ObjPool.Obj<T> poll = null;
        try {
            poll = queue.poll(timeout, unit);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if(poll==null){
            NeedNewFlag flag = NeedNewFlag.getInstance();

            ObjPool.Obj<T> reject = rejectStrategy.reject(flag, constructor, originObjValues);
            if (flag.doINeed()){
                try {
                    T newInstance = constructor.newInstance(originObjValues);
                    return ObjPool.Obj.rejectedInstance(newInstance);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {throw new RuntimeException(e);}
            }else {
                return reject;
            }
        }
        return poll;
    }

    @Override
    public boolean giveBack(ObjPool.Obj<T> obj) {
        if(!obj.isLegal()){
            return false;
        }
        giveBackStrategy.process(obj.getObj());
        queue.offer(obj);
        return true;
    }

    @Override
    public int getCapacity() {
        return nums;
    }

    @Override
    public int getAvailableCapacity() {
        return queue.size();
    }
}
