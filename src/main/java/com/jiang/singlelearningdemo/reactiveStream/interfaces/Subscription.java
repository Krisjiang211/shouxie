package com.jiang.singlelearningdemo.reactiveStream.interfaces;

public interface Subscription {
    void request(long n); // 关键的背压方法
    void cancel();
}