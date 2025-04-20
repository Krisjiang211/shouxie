package com.jiang.singlelearningdemo.myMap;

import java.util.HashMap;
import java.util.Random;

public class HashMapTest {
    public static void main(String[] args) {
        // 要测试的数据量
        int count = 10_000_000;
//        int count = 100000;

        // 准备随机 key 和 value
        Random random = new Random();
        int[] keys = new int[count];
        int[] values = new int[count];
        for (int i = 0; i < count; i++) {
            keys[i] = random.nextInt(count);
            values[i] = random.nextInt(count);
        }

        MyHashMap<Integer, Integer> map = new MyHashMap<>();

        // 插入性能测试
        long startInsert = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            map.put(keys[i], values[i]);
        }
        long endInsert = System.currentTimeMillis();
        System.out.println("插入 " + count + " 条数据耗时: " + (endInsert - startInsert) + " ms");

        // 查询性能测试
        long startQuery = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            map.get(keys[random.nextInt(count)]);
        }
        long endQuery = System.currentTimeMillis();
        System.out.println("查询 " + count + " 次耗时: " + (endQuery - startQuery) + " ms");

        //打印hashMap
//        System.out.println(map);
    }

    public static void main1(String[] args) {
        MyHashMap<String, Integer> map = new MyHashMap<>();
        map.put("1",1);
        System.out.println("map.get(\"1\") = " + map.get("1"));
        System.out.println("map = " + map);
    }
}
