package com.jiang.singlelearningdemo.reactiveStream.interfaces;

public interface Publisher<T> {
    void subscribe(Subscriber<? super T> subscriber);
}




