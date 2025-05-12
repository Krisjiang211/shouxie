package com.jiang.singlelearningdemo.blocking.item;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Item {
    private Core core=null;

    private ReentrantLock lock=new ReentrantLock();
    private Condition canGet=lock.newCondition();

    public Core getCore() throws InterruptedException {
        lock.lock();
        try {
            while (core==null){
                canGet.await();
            }
            return core;
        }finally {
            lock.unlock();
        }
    }

    public void returnCore(Core core){
        lock.lock();
        try {
            this.core=core;
            canGet.signal();
        }finally {
            lock.unlock();
        }
    }


}

class Core{
    public void test(){
        System.out.println("Fuck!!!!!!!!!!!!!!!!!");
    }
}
