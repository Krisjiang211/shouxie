package com.jiang.singlelearningdemo.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RWLock {

    ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    ReentrantReadWriteLock.ReadLock readLock = rwLock.readLock();
    ReentrantReadWriteLock.WriteLock writeLock = rwLock.writeLock();
}
