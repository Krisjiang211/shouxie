package com.jiang.singlelearningdemo.dataStructure.queue;

public class QueueTest {


    public static void main(String[] args) {

        Queue q = new Queue();
        q.enqueue(1);
        q.enqueue(2);
        System.out.println("q.take() = " + q.take());
        System.out.println("q.take() = " + q.take());
        System.out.println("q.take() = " + q.take());
    }
}
