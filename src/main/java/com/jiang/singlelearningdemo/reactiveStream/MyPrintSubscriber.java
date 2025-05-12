package com.jiang.singlelearningdemo.reactiveStream;

import com.jiang.singlelearningdemo.reactiveStream.interfaces.Subscriber;
import com.jiang.singlelearningdemo.reactiveStream.interfaces.Subscription;

// 自定义 Subscriber，控制订阅行为
public class MyPrintSubscriber implements Subscriber<Integer> {
    private Subscription subscription;

    @Override
    public void onSubscribe(Subscription s) {
        this.subscription = s;

        // 开始背压控制：我只要3个
        System.out.println("首次请求3个元素");
        subscription.request(3);

        // 模拟后续再请求更多
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("再请求5个元素");
                subscription.request(5);

                Thread.sleep(1000);
                System.out.println("最后请求100个元素（实际上不够）");
                subscription.request(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    @Override
    public void onNext(Integer item) {
        System.out.println("收到数据: " + item);
    }

    @Override
    public void onError(Throwable t) {
        System.err.println("出错了：" + t);
    }

    @Override
    public void onComplete() {
        System.out.println("全部完成");
    }
}