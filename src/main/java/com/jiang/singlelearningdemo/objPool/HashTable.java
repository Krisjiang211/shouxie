package com.jiang.singlelearningdemo.objPool;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 根据时间戳进行Hash操作.
 * 特点:
 * 1. 静态不扩容
 * 2. 使用CAS锁＋随机数访问错峰竞争
 * 3. 索引快速访问
 * 4. 总数查询快(使用AtomicLong)
 */
public class HashTable<T> implements Collection<HashTable.Elem<T>> {
    private final int size;//总容量
    private final int casThreshold;//CAS锁尝试的阈值, 如果大于该值则走拒绝策略
    private final AtomicInteger availableCount;
    private final Elem<T>[] table;

    private Condition stw=new ReentrantLock().newCondition();
    private volatile boolean isStop=false;

    public HashTable(int size,int casThreshold) {
        this.size = size;
        this.casThreshold=casThreshold;
        availableCount=new AtomicInteger(size);
        table=new Elem[size];
    }

    //默认一直自旋竞争
    public static <T> HashTable<T> newDefault(int size){
        return new HashTable<>(size, Integer.MAX_VALUE);
    }


    //ObjPool.Obj<T>里面的id在这里实质就是索引
    public static class Elem<T> extends ObjPool.Obj<T> {
        private final AtomicBoolean used = new AtomicBoolean(false);//这个用来判断是否在竞争

        public Elem(ObjPool.Obj<T> obj) {
            super(obj.getObj(), obj.getId(), obj.getMsg(), obj.isLegal());
        }

        public boolean isUsed() {
            return used.get();
        }

        public AtomicBoolean getUsed() {
            return this.used;
        }


        public ObjPool.Obj<T> getObjT(){
            return this;
        }

    }

    //这里可以不用加锁, 因为我的主逻辑就是在单线程下完成的所以无需关注线程安全问题
    protected void offer(ObjPool.Obj<T> obj){
        Integer index = obj.getId();
        table[index]=new Elem<>(obj);
    }


    public ObjPool.Obj<T> tryApply(){
        while (isStop){
            try {stw.await();} catch (InterruptedException e) {throw new RuntimeException(e);}
        }
        int index = hash();
        Elem<T> elem = table[index];
        if (elem.getUsed().compareAndSet(false,true)) {
            availableCount.decrementAndGet();
            return elem.getObjT();
        }
        return null;
    }


    public ObjPool.Obj<T> apply(){
        while (isStop){
            try {stw.await();} catch (InterruptedException e) {throw new RuntimeException(e);}
        }

        int index = hash();
        //使用自旋锁来实现
        Elem<T> elem = table[index];
        AtomicBoolean used = elem.getUsed();
        int times=0;
        while (!used.compareAndSet(false,true)){
            //自旋n次依旧无法成功, 返回null, 为下一步拒绝策略准备
            if (times>=casThreshold){
                return null;
            }
            times++;
            try {
                int millis = ThreadLocalRandom.current().nextInt(20, 100);
                Thread.sleep(millis);} catch (InterruptedException e) {throw new RuntimeException(e);}
        }
        availableCount.decrementAndGet();
        return elem.getObjT();

    }


    //从对象池申请一个对象(阻塞, 直到申请到或者超时为止)
    public ObjPool.Obj<T> apply(long timeout, TimeUnit timeUnit){
        //使用循环,防止虚假唤醒
        while (isStop){
            try {stw.await();} catch (InterruptedException e) {throw new RuntimeException(e);}
        }

        int hash = hash();
        long duration = timeUnit.toMillis(timeout);
        long start = System.currentTimeMillis();
        Elem<T> elem = table[hash];

        while (true){
            //是否成功夺舍
            if (elem.getUsed().compareAndSet(false,true)) {
                availableCount.decrementAndGet();
                return elem.getObjT();
            }
            //是否超时
            long now = System.currentTimeMillis();
            if (start+duration<= now){
                return null;
            }
            //随机等待时间
            int waitTimes;
            if (duration<100){
                int halfTime = (int) duration / 2;
                waitTimes=ThreadLocalRandom.current().nextInt(0, halfTime);
            }else {
                waitTimes=ThreadLocalRandom.current().nextInt(0,100);
            }
            try {Thread.sleep(waitTimes);} catch (InterruptedException e) {throw new RuntimeException(e);}
        }

    }



    //归还一个对象
    public boolean giveBack(ObjPool.Obj<T> obj){
        while (isStop){
            try {stw.await();} catch (InterruptedException e) {throw new RuntimeException(e);}
        }

        Integer index = obj.getId();
        AtomicBoolean used = table[index].getUsed();
        used.set(false);
        availableCount.incrementAndGet();
        return true;
    };

    //总容量
    public int getCapacity(){
        return size;
    };

    //当前对象池中可用的容量
    public int getAvailableCapacity(){
        return availableCount.get();
    };


    public ObjPool.Obj<T> getObjByIndex(int index){
        Elem<T> elem = table[index];
        if (!elem.isUsed()) {
            return elem.getObjT();
        }
        return null;
    }

    //强制获取对象
    protected ObjPool.Obj<T> getObjByIndexForce(int index){
        return table[index].getObjT();
    }









    //Hash函数
    public int hash(){
        return ThreadLocalRandom.current().nextInt(0, size);
    }


    private class HashTableIterator implements Iterator<Elem<T>> {

        int cursor=0;
        int length=size;


        @Override
        public boolean hasNext() {

            if (cursor<length) {
                if (cursor==0) stopTheWorld();
                return true;
            }
            resumeTheWorld();
            return false;
        }

        @Override
        public Elem<T> next() {
            return table[cursor++];
        }

        private void stopTheWorld(){
            isStop = true;
        }

        private void resumeTheWorld(){
            isStop = false;
            stw.signalAll();
        }
    }

    @Override
    public Iterator<Elem<T>> iterator() {
        return new HashTableIterator();
    }

    @Override
    public int size() {
        return availableCount.get();
    }







    @Override
    public Object[] toArray() {
        throw new RuntimeException("不支持转换位数组...");
    }
    @Override
    public <T> T[] toArray(T[] a) {
        throw new RuntimeException("不支持转换位数组...");
    }
    @Override
    public boolean isEmpty() {
        throw new RuntimeException("不支持鉴定...");
    }

    @Override
    public boolean contains(Object o) {
        throw new RuntimeException("不支持鉴定...");
    }

    @Override
    public boolean add(Elem<T> tElem) {
        throw new RuntimeException("不支持添加元素...");
    }

    @Override
    public boolean remove(Object o) {
        throw new RuntimeException("不支持删除元素...");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new RuntimeException("不支持鉴定...");
    }

    @Override
    public boolean addAll(Collection<? extends Elem<T>> c) {
        throw new RuntimeException("不支持添加元素...");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new RuntimeException("不支持删除元素...");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new RuntimeException("不支持删除元素...");
    }

    @Override
    public void clear() {
        throw new RuntimeException("不支持删除元素...");
    }

}
