package com.jiang.singlelearningdemo.reactiveStream;

import com.jiang.singlelearningdemo.reactiveStream.interfaces.Publisher;
import com.jiang.singlelearningdemo.reactiveStream.interfaces.Subscriber;
import com.jiang.singlelearningdemo.reactiveStream.interfaces.Subscription;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;


// 自定义 Publisher，发送1到10的整数
public class MyRangePublisher implements Publisher<Integer> {
    private final int start;
    private final int end;

    public MyRangePublisher(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public void subscribe(Subscriber<? super Integer> subscriber) {
        // 创建订阅关系
        MyRangeSubscription subscription = new MyRangeSubscription(start, end, subscriber);
        subscriber.onSubscribe(subscription);
    }

    // 内部 Subscription 实现，真正的数据流控制逻辑
    static class MyRangeSubscription implements Subscription {
        private int index;
        private final int end;
        private final Subscriber<? super Integer> subscriber;
        private final AtomicLong requested = new AtomicLong(0); // 背压计数器
        private final AtomicBoolean canceled = new AtomicBoolean(false);
        private final Thread workerThread;

        public MyRangeSubscription(int start, int end, Subscriber<? super Integer> subscriber) {
            this.index = start;
            this.end = end;
            this.subscriber = subscriber;

            // 启动一个后台线程发送数据
            this.workerThread = new Thread(this::emit);
            this.workerThread.start();
        }

        @Override
        public void request(long n) {
            if (n <= 0) {
                subscriber.onError(new IllegalArgumentException("request(n) must be > 0"));
                return;
            }

            // 增加允许发送的数量
            requested.addAndGet(n);
        }

        @Override
        public void cancel() {
            canceled.set(true);
            workerThread.interrupt();
        }

        // 真正的数据发送逻辑，在独立线程中运行
        private void emit() {
            try {
                while (index <= end) {
                    if (canceled.get()) return;

                    if (requested.get() > 0) {
                        subscriber.onNext(index++);
                        requested.decrementAndGet();
                        Thread.sleep(100); // 模拟发送延迟
                    } else {
                        // 没有 request，不发送，挂起当前线程（非真正阻塞）
                        Thread.sleep(10);
                    }
                }

                subscriber.onComplete();
            } catch (InterruptedException e) {
                // 线程取消或中断
            }
        }
    }
}