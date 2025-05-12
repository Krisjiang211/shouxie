package com.jiang.singlelearningdemo.dataStructure.queue;

public class Queue {
    Node head;
    Node tail;
    int size=0;
    public Queue(){
        //头指针和尾指针都指向一个空节点
        Node dummy = new Node(null);
        head=dummy;
        tail=dummy;
    }


    public Object enqueue(Object data){
        Node newTail = new Node(data);
        //尾插法入队
        Node originTail = tail;
        originTail.next=newTail;
        newTail.pre=originTail;
        tail=newTail;
        size++;
        return data;
    }

    public Object take(){
        if (size==0){
            System.out.println("队列为空, 取不了一点");
            return null;
        }
        Node originHead = head;
        head=head.next;//更新head指针
        Object data = head.data;

        //消除旧head
        head.data=null;
        originHead.next=null;
        head.pre=null;
        size--;
        return data;
    }



    public boolean isEmpty(){
        return size==0;
    }






}

class Node {
    Object data;
    Node pre;
    Node next;
    public Node(Object data) {
        this.data = data;
    }
}
