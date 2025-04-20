package com.jiang.singlelearningdemo.jucTest;

import java.util.concurrent.atomic.AtomicReference;

public class SelfCycleLock {

    AtomicReference<Thread> atomicReference = new AtomicReference<>();

    public void lock(){
        while (!atomicReference.compareAndSet(null,Thread.currentThread()));
    }

    public void unlock(){
        while (!atomicReference.compareAndSet(Thread.currentThread(),null));
    }

    public boolean tryLock(){
        return atomicReference.compareAndSet(null,Thread.currentThread());
    }
}
