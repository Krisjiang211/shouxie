package com.jiang.singlelearningdemo.reactiveStream;


import com.jiang.singlelearningdemo.reactiveStream.interfaces.Publisher;
import com.jiang.singlelearningdemo.reactiveStream.interfaces.Subscriber;

// ---- 测试入口 ----
public class ReactiveStreamsCoreExample {
    public static void main(String[] args) {
        Publisher<Integer> publisher = new MyRangePublisher(1, 10);
        Subscriber<Integer> subscriber = new MyPrintSubscriber();

        publisher.subscribe(subscriber);
    }
}
