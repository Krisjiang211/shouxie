package com.jiang.singlelearningdemo.blocking.queue;


import com.jiang.singlelearningdemo.blocking.item.Item;

import java.util.concurrent.TimeUnit;

public class BlockTest {
    public static void main(String[] args) throws InterruptedException {
        MyBlockingList myBlockingList = new MyBlockingList();
        new Thread(()->{
//            try {TimeUnit.SECONDS.sleep(3);} catch (InterruptedException e) {throw new RuntimeException(e);}
            try {
                for (int i=1;i<100;i++){
                    myBlockingList.put("value"+i);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        new Thread(()->{
            try {TimeUnit.SECONDS.sleep(3);} catch (InterruptedException e) {throw new RuntimeException(e);}
            try {
                myBlockingList.put("jiang");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        for (int i=1;i<101;i++){
            String take = myBlockingList.take();
            System.out.println(take);
//            TimeUnit.MILLISECONDS.sleep(300);
        }

    }
}
