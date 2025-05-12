package com.jiang.singlelearningdemo.aqs;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

/**
 * UnfairLock: 非公平锁
 *
 * 如果要写FairLock, 只需要让所有进来的线程都强制入队即可
 */

public class JiangLock {

    AtomicBoolean inUsing=new AtomicBoolean(false);//竞争指标, 如果为true表示开启竞争
    AtomicReference<Node> head= new AtomicReference<>();
    AtomicReference<Node> tail= new AtomicReference<>();

    volatile Thread owner=null;

    public JiangLock() {
        Node dummy = new Node();
        head.set(dummy);
        tail.set(dummy);
    }

    void lock(){
        Thread currentThread = Thread.currentThread();
        if (inUsing.compareAndSet(false,true)){
            //竞争成功, 直接执行(新来的线程也支持竞争, 不需要排队, 如果竞争失败就排队------非公平锁)
            owner=currentThread;
            return;
        }
        //竞争锁失败, 入队
        Node newNode = new Node(currentThread);
        while (true){
            Node currentTail = tail.get();
            if (tail.compareAndSet(currentTail,newNode)) {
                newNode.pre=currentTail;
                currentTail.next=newNode;
                break;
            }
        }

        /**
         * 入队成功之后, 在此无限循环
         * 在此循环判定是否获得锁(因为再次被唤醒的时候, 不保证一定能获得锁, 如果唤醒还抢不到锁那就继续休眠)
         */
        while (true){
            Node currentHead = head.get();
            if (currentHead.next == newNode
                    && inUsing.compareAndSet(false,true)) {
                //更新owner
                owner=currentThread;
                //唤醒成功, 才更新head
                head.set(newNode);

                return;
            }
            LockSupport.park();
        }

    }

    /**\
     * 释放锁:
     *
     * 核心其实就是
     * 1. 释放竞争指标inUsing
     * 2.1 如果队头有线程, 唤醒他(让他加入竞争), 然后return
     * 2.2 如果队头没有线程, 则直接return.
     */
    void unlock(){
        Thread currentThread = Thread.currentThread();
        if (currentThread != owner) {
            throw new RuntimeException("当前节点未获得锁, 无法解锁");
        }

        owner=null;
        inUsing.set(false);//开启竞争
        Node currentHead = head.get();
        if (currentHead.next!=null){
            LockSupport.unpark(currentHead.next.thread);//唤醒头结点的下一个线程, 让其加入竞争
        }
    }
}

@AllArgsConstructor
@NoArgsConstructor
class Node{
    Node pre;
    Node next;
    Thread thread;

    public Node(Thread thread){
        this.thread=thread;
    }
}