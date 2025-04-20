package com.jiang.singlelearningdemo.iterableTest;

public class IterableTest {
    public static void main(String[] args) {
        for (String s : new User("kris",11)){
            System.out.println(s);
        }
    }
}
