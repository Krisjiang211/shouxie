package com.jiang.singlelearningdemo.threadPool;

import com.jiang.singlelearningdemo.threadPool.core.JiangThreadPool;

public class ThreadPoolTest {


    public static void main(String[] args) throws InterruptedException {
        JiangThreadPool jiangThreadPool = new JiangThreadPool(()->{
            System.out.println("拒绝策略生效, 任务被拒绝后的操作逻辑");
        });
        for (int i = 0; i < 20; i++) {
            int fi=i;
            jiangThreadPool.submit(()->{
                System.out.println(Thread.currentThread().getName()+" 执行了任务"+fi);
            });
        }
    }
}
