package com.jiang.singlelearningdemo.objPool;

import com.jiang.singlelearningdemo.objPool.defaultImpl.DefaultObjPoolUtil;
import com.jiang.singlelearningdemo.objPool.utils.GiveBackStrategy;
import com.jiang.singlelearningdemo.objPool.utils.ObjPoolUtil;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 缺点:
 * 1. 使用 阻塞对列实现, 所有竞争都围绕在队头的元素上性能不好
 * 2. 无法管理已经被使用的对象(比如对象出队之后, 无法视察这个对象的情况)
 *
 *
 * 对象:
 * 分两个对象, 一个是"原始对象", 一个是"包装对象"
 * 最后存入阻塞队列的事"包装对象"
 *
 */
public class ObjPool<T> {
    private Logger log = LoggerFactory.getLogger(ObjPool.class);
    private Class<T> clazz;//对象池中的对象类型
    private String name;//对象池名称
    private DataStructure<T> dataStructure;//数据结构
    private ObjPoolUtil objPoolUtil = new DefaultObjPoolUtil();//其他的一些拓展

    public static <T> ObjPool<T> newDefaultObjPool(String name,
                                                   Class<T> clazz,
                                                   HashMap<Class<?>,?> constructorMap,
                                                   DataStructure<T> dataStructure){
        return new ObjPool<>(name,clazz,constructorMap,dataStructure,null);
    }

    public ObjPool(String name,
                   Class<T> clazz,
                   HashMap<Class<?>,?> constructorMap,
                   DataStructure<T> dataStructure,
                   ObjPoolUtil<T> objPoolUtil){

        this.name=name;
        this.clazz=clazz;
        this.dataStructure=dataStructure;
        this.objPoolUtil = Objects.requireNonNullElseGet(objPoolUtil, () -> new DefaultObjPoolUtil<>());
        if (!initObjsEnhance(constructorMap)) {
            errorCallback(this);
        }else {
            log.info("对象池初始化成功");
        }
    }



    /**
     * 申请使用
     */
    public Obj<T> apply() {
        return dataStructure.apply();
    }


    /**
     * 申请使用(有等待时间)
     */
    public Obj<T> apply(long timeout, TimeUnit unit) {
        return dataStructure.apply(timeout,unit);
    }
    public Obj<T> apply(long waitTimeMs) {
        return apply(waitTimeMs,TimeUnit.MILLISECONDS);
    }

    /**
     * 归还
     */
    public boolean giveBack(Obj<T> obj){
        return dataStructure.giveBack(obj);
    }

    /**
     * 对象池目前状态
     */
    public String getStatus(){
        StringBuilder hashcodes= new StringBuilder();
        if (dataStructure.container.size()>0){
            for (Obj<T> obj : dataStructure.container) {
                hashcodes.append(obj.hashCode()).append(" ,");
            }
            hashcodes = new StringBuilder(hashcodes.substring(0, hashcodes.length() - 2));
        }
        return "对象池名称: "+name+"\n"+
                "对象池总大小: "+this.dataStructure.nums+"\n"+
                "当前对象池可用对象数量: "+String.valueOf(dataStructure.getAvailableCapacity())+"\n"+
                "当前时间: "+objPoolUtil.dateFormat(LocalDateTime.now())+"\n"+
                "可用对象hashcode: "+ "["+hashcodes+"]";
    }





    public boolean initObjsEnhance(HashMap<Class<?>,?> map){

        try {
            return initObjs(map);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            log.error("为构造器赋值的时候出错, 错误原因: "+e.getMessage());
            return false;
        }
    }



    /**
     * 初始化nums个合法对象
     */
    public boolean initObjs(HashMap<Class<?>,?> paramAndValueMap) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        //1. 处理map
        Class<?>[] paramTypes= new Class<?>[paramAndValueMap.size()];
        dataStructure.originObjValues = new Object[paramAndValueMap.size()];
        if (paramAndValueMap.size()>0){
            int j=0;
            for (Class<?> paramType : paramAndValueMap.keySet()) {
                paramTypes[j]=paramType;
                dataStructure.originObjValues[j]=paramAndValueMap.get(paramType);
                j++;
            }
        }
        //2. 拿到构造器
        dataStructure.constructor = clazz.getConstructor(paramTypes);
        dataStructure.constructor.setAccessible(true);
        //3. 利用构造器开始创造对象并入队
        for (int i = 0; i < dataStructure.nums; i++) {
            T instance = dataStructure.constructor.newInstance(dataStructure.originObjValues);
            Obj<T> obj = Obj.getInstance(instance, i);
            dataStructure.offer(obj);
        }
        return true;
    }




    private static <T> void errorCallback(ObjPool<T> objPool){
        objPool.log.error("对象池出问题了");
        objPool=null;
    }

    private static <T> void errorCallback(ObjPool<T> objPool, String msg){
        objPool.log.error("对象池出问题了, 错误原因: "+msg);
        objPool=null;
    }





    @ToString
    public static class Obj<T>{
        private Integer id;//包装对象的id
        private String msg;//包装对象的错误信息

        private T obj;

        private boolean isLegal;//是否是触发拒绝策略的


        private Obj(){}

        public static <T> Obj<T> getInstance(T t){
            Obj<T> instance = new Obj<>();
            instance.obj=t;
            instance.id=-2;
            instance.isLegal=false;
            instance.msg="USER_CONSTRUCT_____ILLEGAL";
            return instance;
        }

        private static <T> Obj<T> getInstance(T t,Integer id){
            Obj<T> instance = new Obj<>();
            instance.obj=t;
            instance.id=id;
            instance.isLegal=true;
            instance.msg="LEGAL_CONSTRUCT";
            return instance;
        }

        protected static <T> Obj<T> rejectedInstance(T t){
            Obj<T> instance = new Obj<>();
            instance.obj=t;
            instance.id=-1;
            instance.isLegal=false;
            instance.msg="REJECTED_STRATEGY_CONSTRUCT";
            return instance;
        }




        public Integer getId() {
            return id;
        }

        public T getObj() {
            return obj;
        }

        public boolean isLegal() {
            return this.isLegal;
        }

    }

}
