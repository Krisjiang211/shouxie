package com.jiang.singlelearningdemo.myMap;


import lombok.extern.slf4j.Slf4j;

/**
 * hashMap实现的Map
 *
 *      横向是k-v数组
 *      出现hash冲突用红黑树填充(我们这里用链表)
 *
 */
@SuppressWarnings("unchecked")
@Slf4j
public class MyHashMap<K,V> {
    int updateTimes =0;

    Integer initSize=2;
    Node<K,V>[] table=new Node[initSize];//键值对的size
    int size=0;


    //若为修改, 返回上一次的 value
    //若为添加, 返回当前value
    public V put(K key,V value){
        int index = indexOf(key);
        //判断是否需要扩容
        if (isNeedMoreBigger()) {
            doTableBigger();
//            log.info("成功执行一次HashMap扩容, 扩容至"+table.length);
        };
        //1. 判断是否存在这个key
        Node<K, V> current = table[index];
        while (current!=null){
            if (current.key.equals(key)){
                V oldV = current.value;
                current.value=value;
                updateTimes++;
                return oldV;
            }
            current=current.next;
        }
        //不存在添加, size++
        //头插法插入
        Node<K, V> newNode = new Node<>(key, value);
        Node<K, V> head = table[index];
        table[index]=newNode;
        newNode.next=head;
//        System.out.println("插入了一个"+current);
        size++;
        return value;
    }


    public V get(K key){
        int index = indexOf(key);
        Node<K, V> current = table[index];
        while (current!=null){
            if (current.key.equals(key)){
                return current.value;
            }
            current=current.next;
        }
        return null;
    }


    public V remove(K key){
        int index = indexOf(key);
        Node<K, V> current= table[index];
        Node<K,V> pre=null;
        while (current!=null){
            if (current.key.equals(key)){
                //在首部
                if (pre==null){
                    table[index]=current.next;
                    return current.value;
                }
                //中间
                else if (pre!=null&&current.next!=null){
                    pre.next=current.next;
                    return current.value;
                }
                //尾部
                else if (pre!=null&&current.next==null){
                    pre.next=null;
                    return current.value;
                }
            }
            //更新指针
            pre=current;
            current=current.next;
        }
        return null;
    }

    public String toString(){
        int count=0;
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i=0;i<table.length;i++){
            Node<K, V> current = table[i];
            while (current!=null){
                sb.append("{"+current.key+":"+current.value+"}"+",\n");
                count++;
                current=current.next;
            }
        }
        sb.append("]"+"\n总共 "+count+" 条数据"+"\n有 "+updateTimes+" 次覆盖");
        return sb.toString();
    }

    //扩容判断
    private boolean isNeedMoreBigger(){
        return size > table.length*0.75;
    }

    private int indexOf(K key){
        return indexOf(key, table.length);
    }
    private int indexOf(K key, int length){
        return key.hashCode() % length;
    }

    //执行扩容
    private void doTableBigger(){
//        log.info("扩容前:"+toString());
        int newSize = table.length * 2;
        Node<K,V>[] newTable = new Node[newSize];
        for (int i=0;i< table.length;i++){
            Node<K,V> current=table[i];
            while (current!=null){
                //以下是为新表的新节点赋值
                int index = indexOf(current.key, newSize);
//                Node<K, V> newNode = newTable[index];
//                while (newNode!=null){
//                    newNode=newNode.next;
//                }
                Node<K, V> next = current.next;
                //依旧头插法
                Node<K, V> head = newTable[index];
                newTable[index]=current;
                current.next=head;

                current=next;
            }
        }
        table=newTable;
//        log.info("扩容后:"+toString());
    }


}

class Node<K,V>{
    K key;
    V value;
    Node<K,V> next;

    public Node(K k, V v){
        this.key=k;
        this.value=v;
    }
    public String toString(){
        return "{key:"+key+",value:"+value+"}";
    }
}
