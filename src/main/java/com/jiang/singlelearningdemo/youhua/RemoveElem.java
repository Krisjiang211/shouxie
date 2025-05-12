package com.jiang.singlelearningdemo.youhua;

import java.util.*;

public class RemoveElem {

    List<Integer> bigList=new ArrayList<>();
    List<Integer> subList=new ArrayList<>();
    public static void main(String[] args) {
        RemoveElem removeElem = new RemoveElem();
        removeElem.initList();
        long startTime = System.currentTimeMillis();
        /**
         * 1. 遍历
         * 999999条数据, 耗时: 72897ms
         */
        for (int i = 0; i < removeElem.bigList.size(); i++) {
            Integer integer = removeElem.bigList.get(i);
            if (removeElem.subList.contains(integer)) {
                removeElem.bigList.remove(i);
                i--;
            }
        }

        /**
         * 2. 使用HashSet
         * 999999条数据, 耗时: 51ms
         */
//        Set<Integer> subSet = new HashSet<>(removeElem.subList);
//        removeElem.bigList.removeIf(subSet::contains);

        long endTime = System.currentTimeMillis();
        System.out.println("totalTime = " + (endTime - startTime)+" ms");

    }

    public void initList(){
        Random random = new Random();
        for (int i = 0; i < 999999; i++) {
            bigList.add(i);
            Boolean aBoolean = (random.nextInt(0, 10) % 10) == 0 ? subList.add(i) : null;
        }
    }

}
