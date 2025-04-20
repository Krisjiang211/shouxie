package com.jiang.singlelearningdemo.hashcode;

import com.jiang.singlelearningdemo.common.pojo.User;

public class HashCodeTest {

    public static void main1(String[] args) {
        Student studentA = new Student("A",1);
        Student studentB = new Student("A",1);
        System.out.println("studentA.hashCode() = " + studentA.hashCode());
        System.out.println("studentB.hashCode() = " + studentB.hashCode());
        System.out.println("studentA.equals(studentB) = " + studentA.equals(studentB));
        System.out.println("studentA == studentB = " + (studentA == studentB));
    }

    public static void main(String[] args) {
        String okA = "ok[gfhn dcsml;fdbjln;mcfvbod  ws/";
        String okB = new String("ok[gfhn dcsml;fdbjln;mcfvbod  ws/");
        System.out.println("okA.hashCode() = " + okA.hashCode());
        System.out.println("okB.hashCode() = " + okB.hashCode());
        System.out.println("okA.equals(okB) = " + okA.equals(okB));

        Integer intA = 100; // 自动装箱，会使用缓存
        Integer intB = 100; // 自动装箱，也会使用缓存
        Integer intC = 200; // 自动装箱，不在缓存范围内
        Integer intD = 200; // 自动装箱，不在缓存范围内

        System.out.println("intA == intB: " + (intA == intB)); // true
        System.out.println("intC == intD: " + (intC == intD)); // false

        Integer intE = Integer.valueOf(498191); // new 创建的 Integer 对象
        Integer intE1 = 498191; // new 创建的 Integer 对象
        System.out.println("intE1.hashCode() = " + intE1.hashCode());
        System.out.println("intE.hashCode() = " + intE.hashCode());
    }

}
