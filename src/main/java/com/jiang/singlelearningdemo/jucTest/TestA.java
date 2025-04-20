package com.jiang.singlelearningdemo.jucTest;

import org.junit.Test;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class TestA {

    public int a=1;

    @Test
    public void ok() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            System.out.println("线程启动!!!!!!!!");
            while (a == 1) {
                System.out.println("我还在运动");
//                try {
//                    TimeUnit.MILLISECONDS.sleep(100);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
            }
            System.out.println("线程结束!!!!!!!!!!!!!!!");
        });


        Thread t2 = new Thread(() -> {
            System.out.println("线程2启动!!!!!!!!");
            a = 5;
            System.out.println("赋值a为: " + a);
        });
        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }
}
