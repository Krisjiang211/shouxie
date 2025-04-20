package com.jiang.singlelearningdemo.threadPool.core;

import com.jiang.singlelearningdemo.jucTest.SelfCycleLock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class JiangThreadPool {

    private SelfCycleLock lock=new SelfCycleLock();//自旋锁

    private int corePoolSize=3;//核心线程数

    private ArrayBlockingQueue<Runnable> delayQueue=new ArrayBlockingQueue<>(12);//任务队列(阻塞队列)

    private int supportPoolSize=2;


    private List<Thread> threads=new ArrayList<>();

    //由于自旋锁保证了线程安全, 所以这里不需要加锁
    private int coreThreadNameNum=0;
    private AtomicInteger supportThreadNameNum=new AtomicInteger(0);//由于在空闲的时候会销毁supportThread, 所以这里需要加锁, 保证这里的线程安全

    private RejectHandler rejectHandler;
    public JiangThreadPool(RejectHandler rejectHandler){
        this.rejectHandler=rejectHandler;
    }

    private Thread coreThreadRun() {
        return new Thread(() -> {
            while (true) {
                try {
                    Runnable runnable = delayQueue.take();
                    runnable.run();
                } catch (InterruptedException e) {throw new RuntimeException(e);}
            }
        },"coreThread-"+coreThreadNameNum++);
    }

    private Thread supportThreadRun(){
        return new Thread(()->{
            while (true){
                try {
                    Runnable command = delayQueue.poll(5, TimeUnit.SECONDS);
                    if(command!=null){
                        command.run();
                    } else {
                        supportThreadNameNum.decrementAndGet();
                        System.out.println("当前空闲, supportThread-"+supportThreadNameNum.get()+" 销毁");
                        threads.remove(Thread.currentThread());//从List<Thread>中移除
                        break;//如果任务队列在5秒后依旧没有取到任务, 那么我们此时认为线程不忙碌, 销毁当前supportThread, 就退出循环
                    }
                } catch (InterruptedException e) {throw new RuntimeException(e);}
            }
        },"supportThread-"+supportThreadNameNum.getAndIncrement());
    }

    public void submit(Runnable command) throws InterruptedException {
        //offer()功能和add()一样, 只不过add()添加失败会报错, offer会返回false
        boolean offer = delayQueue.offer(command);

        //线程安全的创建核心线程--自旋锁保证
        lock.lock();
        try {
            if(threads.size()<corePoolSize){
                Thread t = coreThreadRun();//new 一个核心线程
                threads.add(t);
                t.start();
            } else if (threads.size() < corePoolSize + supportPoolSize) {
                Thread thread = supportThreadRun();
                threads.add(thread);
                thread.start();
            }
        }finally {
            lock.unlock();
        }

        //拒绝策略
        if(!offer){
            rejectHandler.handle();
        }
    }


}
