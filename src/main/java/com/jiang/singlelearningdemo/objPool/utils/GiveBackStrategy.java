package com.jiang.singlelearningdemo.objPool.utils;

public interface GiveBackStrategy<T> {
    public void process(T t);
}
