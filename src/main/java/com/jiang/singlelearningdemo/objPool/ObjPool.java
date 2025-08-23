package com.jiang.singlelearningdemo.objPool;

import com.jiang.singlelearningdemo.objPool.defaultImpl.DefaultObjPoolUtil;
import com.jiang.singlelearningdemo.objPool.defaultImpl.DefaultRejectStrategy;
import com.jiang.singlelearningdemo.objPool.domain.NeedNewFlag;
import com.jiang.singlelearningdemo.objPool.utils.GiveBackStrategy;
import com.jiang.singlelearningdemo.objPool.utils.ObjPoolUtil;
import com.jiang.singlelearningdemo.objPool.utils.RejectStrategy;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
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

    private T originInitObj;//保留一个本体

    private int nums;//对象池中的对象数量
    private Class<T> clazz;//对象池中的对象类型
    private String name;//对象池名称

    private GiveBackStrategy<T> giveBackStrategy;//对象归还策略

    private ObjPoolUtil objPoolUtil = new DefaultObjPoolUtil();//其他的一些拓展

    private RejectStrategy<T> rejectStrategy;//拒绝策略

    private final ArrayBlockingQueue<Obj<T>> queue;

    private Constructor<T> constructor;//原始对象的构造器

    private Object[] originObjValues;//原始对象的参数值

    public static <T> ObjPool<T> newDefaultObjPool(String name,
                   int nums,
                   Class<T> clazz,
                   HashMap<Class<?>,?> constructorMap,
                   GiveBackStrategy<T> giveBackStrategy){
        return new ObjPool<>(name,nums,clazz,constructorMap,giveBackStrategy, null,null);
    }

    public ObjPool(String name,
                   int nums,
                   Class<T> clazz,
                   HashMap<Class<?>,?> constructorMap,
                   GiveBackStrategy<T> giveBackStrategy,
                   RejectStrategy<T> rejectStrategy,
                   ObjPoolUtil<T> objPoolUtil){
        if (nums<=0){
            nums=1;
        }
        this.name=name;
        this.nums=nums;
        this.clazz=clazz;
        this.queue= new ArrayBlockingQueue<>(nums);

        if (giveBackStrategy==null){
            errorCallback(this,"GiveBackStrategy不能为空");
        }else {
            this.giveBackStrategy=giveBackStrategy;
        }
        if (rejectStrategy==null){
            log.warn("对象池初始化-----拒绝策略为空, 将使用默认拒绝策略(如果借对象失败, 将new一个新的对象, 且该对象不受对象池管理)");
            this.rejectStrategy=new DefaultRejectStrategy<>();
        }else {
            this.rejectStrategy=rejectStrategy;
        }


        if (objPoolUtil==null){
            this.objPoolUtil = new DefaultObjPoolUtil<>();
        }else {
            this.objPoolUtil=objPoolUtil;
        }

        if (!initObjsEnhance(constructorMap)) {
            errorCallback(this);
        }else {
            log.info("对象池初始化成功");
        }
    }



    /**
     * 申请使用
     */
    public Obj<T> apply() throws InterruptedException {
        return queue.take();
    }


    /**
     * 申请使用(有等待时间)
     */
    public Obj<T> apply(long timeout, TimeUnit unit) throws InterruptedException {
        Obj<T> poll = queue.poll(timeout, unit);
        if(poll==null){
            NeedNewFlag flag = NeedNewFlag.getInstance();
            Obj<T> reject = rejectStrategy.reject(flag, constructor, originObjValues);
            if (flag.doINeed()){
                try {
                    T newInstance = constructor.newInstance(originObjValues);
                    return Obj.rejectedInstance(newInstance);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {throw new RuntimeException(e);}
            }else {
                return reject;
            }
        }
        return poll;
    }
    public Obj<T> apply(long waitTimeMs) throws InterruptedException {
        return apply(waitTimeMs,TimeUnit.MILLISECONDS);
    }

    /**
     * 归还
     */
    public void giveBack(Obj<T> obj){
        if(!obj.isLegal){
            return;
        }
        giveBackStrategy.process(obj.getObj());
        queue.offer(obj);
    }

    /**
     * 对象池目前状态
     */
    public String getStatus(){
        String hashcodes="";
        if (queue.size()>0){
            for (Obj<T> obj : queue) {
                hashcodes+=obj.hashCode()+" ,";
            }
            hashcodes=hashcodes.substring(0,hashcodes.length()-2);
        }
        return "对象池名称: "+name+"\n"+
                "对象池总大小: "+this.nums+"\n"+
                "当前对象池可用对象数量: "+String.valueOf(queue.size())+"\n"+
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
        originObjValues = new Object[paramAndValueMap.size()];
        if (paramAndValueMap.size()>0){
            int j=0;
            for (Class<?> paramType : paramAndValueMap.keySet()) {
                paramTypes[j]=paramType;
                originObjValues[j]=paramAndValueMap.get(paramType);
                j++;
            }
        }
        //2. 拿到构造器
        constructor = clazz.getConstructor(paramTypes);
        constructor.setAccessible(true);
        //3. 利用构造器开始创造对象并入队
        for (int i = 0; i < nums; i++) {
            T instance = constructor.newInstance(originObjValues);
            Obj<T> obj = Obj.getInstance(instance, i);
            queue.offer(obj);
        }

        //4. 为本体赋值
        originInitObj=constructor.newInstance(originObjValues);

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

        private static <T> Obj<T> rejectedInstance(T t){
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

        private void setIsLegal(boolean isLegal) {
            this.isLegal = isLegal;
        }

    }

}
