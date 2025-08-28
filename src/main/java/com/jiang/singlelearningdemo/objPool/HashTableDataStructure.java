package com.jiang.singlelearningdemo.objPool;

import com.jiang.singlelearningdemo.objPool.domain.NeedNewFlag;
import com.jiang.singlelearningdemo.objPool.utils.GiveBackStrategy;
import com.jiang.singlelearningdemo.objPool.utils.RejectStrategy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

public class HashTableDataStructure<T> extends DataStructure<T> {
    private final HashTable<T> table;
    public HashTableDataStructure(int nums, Constructor<T> constructor, Object[] originObjValues, RejectStrategy<T> rejectStrategy, GiveBackStrategy<T> giveBackStrategy) {
        super(nums, constructor, originObjValues, rejectStrategy, giveBackStrategy);
        table=new HashTable<>(nums,5);
        container=table;
    }

    public static <T> HashTableDataStructure<T> newDefault(int nums,  RejectStrategy<T> rejectStrategy, GiveBackStrategy<T> giveBackStrategy){
        return new HashTableDataStructure<>(nums,null,null,rejectStrategy,giveBackStrategy);
    }

    public static <T> HashTableDataStructure<T> newDefault(int nums, GiveBackStrategy<T> giveBackStrategy){
        return newDefault(nums,null,giveBackStrategy);
    }

    @Override
    protected void offer(ObjPool.Obj<T> obj) {
        table.offer(obj);
    }

    @Override
    public ObjPool.Obj<T> tryApply(){
        return table.tryApply();
    }

    @Override
    public ObjPool.Obj<T> apply() {
        return table.apply();
    }

    @Override
    public ObjPool.Obj<T> apply(long timeout, TimeUnit timeUnit) {
        ObjPool.Obj<T> poll;
        poll = table.apply(timeout, timeUnit);
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
        table.offer(obj);
        return true;
    }

    @Override
    public int getCapacity() {
        return table.getCapacity();
    }

    @Override
    public int getAvailableCapacity() {
        return table.getAvailableCapacity();
    }
}
