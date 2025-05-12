package com.jiang.singlelearningdemo.aqs;

import java.util.Queue;
import java.util.concurrent.CountDownLatch;

public class LockTest {

    private static Integer num=100;
    static CountDownLatch latch=new CountDownLatch(num);

    public static void main(String[] args) throws InterruptedException {
        JiangLock lock = new JiangLock();

        for (int i = 0; i < 100; i++) {

            new Thread(()->{
                lock.lock();
                num--;
                lock.unlock();
                latch.countDown();
            },"t-"+i).start();
        }
        latch.await();
        System.out.println("num = " + num);

    }

}
