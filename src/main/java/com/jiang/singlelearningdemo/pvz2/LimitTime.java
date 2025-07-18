package com.jiang.singlelearningdemo.pvz2;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 这个类用来模拟PVZ2的限时任务
 * 在5s内击杀6个僵尸算完成任务
 */
public class LimitTime<T> {

    private long limitTimeMS;//限制任务时间, 单位:ms

    private long winValues;//任务指标是几个

    private boolean isSuccess;//是否完成任务

    private long startTime;//任务开始时间

    private long successTime;//任务完成时间

    private final AtomicLong addTimes=new AtomicLong(0L);//添加元素次数

    public LimitTime(long limitTimeMS, long winValues){
        this.limitTimeMS=limitTimeMS;
        this.winValues=winValues;
    }


    //我需要维护一个队列(并发队列)
    private final ConcurrentLinkedQueue<Item<T>> queue=new ConcurrentLinkedQueue<>();




    public void start(){
        startTime=System.currentTimeMillis();
    }


    //加入新的完成指标
    public void add(Item<T> item){
        if (addTimes.get()==0L){
            startTime=System.currentTimeMillis();
        }
        //如果已完成任务直接退出
        if (isSuccess) return;
        queue.add(item);
        isFinish();
        addTimes.incrementAndGet();
    }

    //是否完成任务
    public boolean isFinish(){
        checkAndRefresh();
        isSuccess = winValues == queue.size();
        if (isSuccess){
            successTime=System.currentTimeMillis();
        }
        return isSuccess;
    }

    //每次刷新的时候, 都检查一下队列中的元素是否过期, 过期的踢出
    public void checkAndRefresh(){
        while (true){
            Item<T> item = queue.peek();
            if (item==null) return;

            //一直刷新元素, 直到找到第一个未过期的元素
            long itemMs = item.getMs();
            if (isInLimitTime(itemMs)){
                break;
            }
            queue.poll();
        }
    }


    private boolean isInLimitTime(long ms){
        return System.currentTimeMillis()-ms<=limitTimeMS;
    }



    //获取任务进度
    public String getProgress(){
        if (startTime==0L) return "任务未开始, 请先开始任务";
        if (isSuccess){
            return "任务完成, 耗时"+(successTime-startTime)+"ms"+"\n"+
                    "总添加次数"+addTimes.get();
        }
        return  "任务未完成, 进度" + queue.size() + "/" + winValues +"\n"+
                "耗时" + (System.currentTimeMillis() - startTime) + "ms"+"\n"+
                "当前总添加次数"+addTimes.get();
    }


}
