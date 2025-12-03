package com.jiang.singlelearningdemo.skipList;

import lombok.Data;

import java.util.*;

/**
 *  跳表
 */
public class SkipList {

    private final LinkedList<Elem> list;//数据层
    private final LinkedList<NodeLevel> levels = new LinkedList<>();//跳表的层级，每个层级都是一个有序的链表, 当最顶层链表只有一个节点时候, 不再增加层级
    private final Random random = new Random();
    public SkipList(LinkedList<Elem> list){
        this.list = list;
        sort(this.list);
        initLevels(list, levels);
    }
    public SkipList(){
        this.list = new LinkedList<>();
    }


    //初始化跳表层级
    private void initLevels(LinkedList<Elem> list,LinkedList<NodeLevel> levels){
        if (list.isEmpty()) return;
        NodeLevel currentSwapList = null;//每轮for循环前要循环的链表
        NodeLevel nextSwapList = new NodeLevel();//每一轮循环后, 会化为索引的元素组成的链表

        Node preNode = null;
        do {
            //预热一下, 预热出第一层索引
            for (Elem elem : this.list) if (toBeIndex()) preNode = nextSwapList.addNext(preNode, elem, null);//
            levels.add(nextSwapList);//第一层索引
            currentSwapList = nextSwapList;
        } while (nextSwapList.size == 0);

        while (!(levels.getLast().size() <=1)|| levels.getLast().isEmpty()){
            preNode=null;
            nextSwapList.clear();
            for (Node node : currentSwapList) if(toBeIndex()) preNode = nextSwapList.addNext(preNode, node.elem, node);//更新node
            //必须不能让最顶层是空的. 我们的想法是在顶层有且仅有1个节点
            if (nextSwapList.size!=0){
                levels.add(nextSwapList);
                currentSwapList = nextSwapList;
            }
        }

    }


    public Elem get(Long id){
        Node currentNode = levels.getLast().headNode;
        while (currentNode != null){
            Long currNodeId = currentNode.getElem().getId();
            if (id.equals(currNodeId)){
                return currentNode.getElem();
            }
            //大于情况
            else if (id > currNodeId){
                Node right = currentNode.right;
                if (right!=null) currentNode = right;
                else {
                    Node below = currentNode.below;
                    currentNode = below;
                    if (below == null){
                        return null;
                    }
                }
            }
            //小于情况
            else {
                Node left = currentNode.left;
                if (left!=null) currentNode = left;
                else {
                    Node below = currentNode.below;
                    currentNode = below;
                    if (below == null){
                        return null;
                    }
                }
            }
        }
        return null;
    }

    public Object getObj(Long id){
        return get(id).getData();
    }


    public List<Object> getDataByRange(Long start, Long end){
        if (start==null||end==null||start<end) return new ArrayList<>();
        Elem elem = get(start);
        return List.of();
    }




    //是否成为索引
    private boolean toBeIndex(){
        return random.nextInt(2) == 0;
    }



    private void sort(LinkedList<Elem> list){
        //TODO 排序待实现
    }

    @Data
    public static class NodeLevel implements Iterable<Node>{
        private int size = 0;
        private Node headNode = null;

        public int size(){
            return size;
        }

        public NodeLevel(Elem elem, Node belowNode){
            Node newHeadNode = Node.headNode(elem);
            this.headNode = newHeadNode;
            newHeadNode.below= belowNode;
            this.size = 1;
        }

        public NodeLevel(){}

        public Node addNext(Node preNode, Elem e, Node belowNode){
            //头节点插入
            if (preNode == null){
                Node newHeadNode = Node.headNode(e);
                this.headNode = newHeadNode;
                newHeadNode.below= belowNode;
                this.size = 1;
                return newHeadNode;
            }
            Node node = new Node();
            node.elem = e;

            preNode.right = node;
            node.left = preNode;
            node.below = belowNode;
            size++;
            return node;
        }

        public void removeNode(Node node) {
            if (node == null) return;

            //头结点
            if (Node.isHeadNode(node)){
                //头结点右边还有节点, 需要将其更新为头结点
                if (node.right!=null){
                    node.right.left = null;
                }
            }
            //尾节点
            else if (Node.isTailNode(node)){
                node.left.right = null;
            }
            //中间节点
            else {
                node.left.right = node.right;
                node.right.left = node.left;
            }
            // 清空节点本身
            node.right = null;
            node.left = null;
            node.elem = null;
            node.below = null;
            size--;
        }






        public boolean isEmpty(){
            return size == 0;
        }

        @Override
        public Iterator<Node> iterator() {
            return new NodeListIterator();
        }

        public void clear() {
            Node currentNode = headNode;
            while (currentNode!=null){
                removeNode(currentNode);
                currentNode = currentNode.right;
            }
            size = 0;
        }

        class NodeListIterator implements Iterator<Node>{
            private Node currentNode = headNode;

            @Override
            public boolean hasNext() {
                return Node.hasNext(currentNode);
            }

            @Override
            public Node next() {
                if (currentNode == null){
                    throw new NoSuchElementException();
                }
                Node node = currentNode;
                currentNode = currentNode.right;
                return node;
            }
        }
    }



    @Data
    public static class Node{
        private Node left;//左侧节点, 前一个节点
        private Node right;//右侧节点, 下一个节点
        private Node below;//下层的节点
        private Elem elem;

        public static  Node headNode(Elem elem){
            Node node = new Node();
            node.left=null;
            node.right=null;
            node.elem = elem;
            return node;
        }
        public static  boolean isHeadNode(Node node){
            return node.left==null ;
        }

        //我们规定如果一层只有一个节点, 那么他只能是头节点
        public static  boolean isTailNode(Node node){
            return node.right == null && node.left != null;
        }

        public static  boolean hasNext(Node node){
            return node != null && node.right != null;
        }

        public static  boolean hasPrevious(Node node){
            return node.left != null;
        }


    }

}
