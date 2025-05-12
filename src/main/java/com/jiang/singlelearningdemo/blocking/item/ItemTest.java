package com.jiang.singlelearningdemo.blocking.item;

import java.util.concurrent.TimeUnit;

public class ItemTest {


    public static void main(String[] args) throws InterruptedException {
        Item item = new Item();
        new Thread(()->{
            try {
                item.getCore().test();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        System.out.println("睡3秒之后, 放入Core");
        TimeUnit.SECONDS.sleep(3);
        item.returnCore(new Core());

    }


}
