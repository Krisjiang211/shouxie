package com.jiang.singlelearningdemo.blocking.queue;

import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MyBlockingList {

//    private final String[] table=new String[1024];
//    int maxSize=1024;//最大存储容量
    int size=0;//有效元素个数
    Node headNode=new Node(null,null);//队头指针
    Node tailNode=new Node(null,null);//队尾指针

    ReentrantLock lock=new ReentrantLock();

    Condition notEmpty=lock.newCondition();


    public String put(String value) throws InterruptedException {
        Node newNode = new Node(value, null);
        lock.lock();
        try {
            if (isEmpty()){
                headNode.next=newNode;
                tailNode.next=newNode;
            }else {
                Node originNode = tailNode.next;
                originNode.next=newNode;
                tailNode.next=newNode;
            }
            size++;
            notEmpty.signal();
            return value;
        }finally {
            lock.unlock();
        }
    }


    public String take() throws InterruptedException {
        //1. 判空
        //注意: 使用while是为了防止线程被虚假唤醒
        lock.lock();
        while (isEmpty()){
            notEmpty.await();
        }
        try {
            Node originNode = headNode.next;
            headNode.next=originNode.next;
            String value = originNode.value;
            if (headNode.next==null){
                tailNode.next=null;//同步队尾指针
            }
            size--;
            return value;
        }finally {
            lock.unlock();
        }
    }




    private boolean isEmpty(){
//        return size==0;
        return headNode.next==null&&tailNode.next==null;
    }


    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        Node currentNode=headNode.next;
        while (currentNode!=null){
            sb.append(currentNode.value+" --- ");
            currentNode=currentNode.next;
        }
        return sb.toString();
    }

}

@AllArgsConstructor
@ToString
class Node{
    String value;
    Node next;
}
