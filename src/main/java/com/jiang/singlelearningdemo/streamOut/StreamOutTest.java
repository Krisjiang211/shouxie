package com.jiang.singlelearningdemo.streamOut;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class StreamOutTest {

    static BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>();
    static String res="";
     static boolean isCompleted = false;
    public static void main(String[] args) {
        new Thread(()->{
            System.out.println("我是生产者");
            for (int i = 0; i < 10; i++) {
                try {TimeUnit.MILLISECONDS.sleep(200);} catch (InterruptedException e) {throw new RuntimeException(e);}
                Integer num = new Random().nextInt(16565112);
                blockingQueue.add(num.toString());
                res+=num.toString();
            }
            isCompleted=true;
            }).start();

        new Thread(()->{
            while (!isCompleted){
                try {
                    String take = blockingQueue.take();
                    System.out.println("拿到一次数据"+take);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("消费者任务结束, 结果是"+res);
        }).start();
    }



}
