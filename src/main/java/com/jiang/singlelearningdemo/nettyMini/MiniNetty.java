package com.jiang.singlelearningdemo.nettyMini;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Mini版Netty，核心功能：
 * - Selector监听多个Channel
 * - Channel有数据就注册到Selector
 * - Selector拿到活，把任务丢到线程池
 * - 线程池处理后，把结果丢到阻塞队列
 * - 最后统一消费处理结果
 */
public class MiniNetty {

    public static void main(String[] args) {
        Selector selector = new Selector();

        // 创建几个模拟的 Channel
        for (int i = 0; i < 5; i++) {
            Channel channel = new Channel("channel-" + i, selector);
            selector.register(channel);
        }

        // 启动 Selector 主线程
        new Thread(selector::eventLoop, "Selector-Thread").start();

        // 模拟外部有数据到达
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                selector.getChannels().forEach(Channel::fireReadEvent);
            }
        }, 1000, 2000); // 每2秒所有channel触发一次read事件
    }
}

class Selector {

    private final Set<Channel> channels = new HashSet<>();
    private final BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();
    private final ExecutorService workerPool = Executors.newFixedThreadPool(3);

    public void register(Channel channel) {
        channels.add(channel);
    }

    public Set<Channel> getChannels() {
        return channels;
    }

    /**
     * 核心方法：不断轮询
     */
    public void eventLoop() {
        System.out.println("Selector启动，开始监听Channel事件...");

        while (true) {
            try {
                // 模拟阻塞等待事件
                Runnable task = taskQueue.take();
                task.run(); // 直接运行处理逻辑
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 被Channel调用，告诉Selector：我有数据了
     */
    public void wakeup(Channel channel) {
        taskQueue.offer(() -> {
            System.out.println(Thread.currentThread().getName() + ": 收到 " + channel.getName() + " 的通知, 提交线程池处理");
            workerPool.submit(() -> {
                channel.handleRead();
            });
        });
    }
}

class Channel {

    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(0);
    private final String name;
    private final Selector selector;

    public Channel(String name, Selector selector) {
        this.name = name;
        this.selector = selector;
    }

    public String getName() {
        return name;
    }

    /**
     * 模拟Channel触发可读事件
     */
    public void fireReadEvent() {
        System.out.println(name + ": 有数据到达, 准备唤醒Selector");
        selector.wakeup(this);
    }

    /**
     * 真正处理Channel数据
     */
    public void handleRead() {
        System.out.println(Thread.currentThread().getName() + ": 正在处理 " + name + " 的数据");
        try {
            Thread.sleep(500); // 模拟业务处理耗时
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + ": 处理完毕 " + name);
    }
}

