package com.jiang.singlelearningdemo.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SystemTest {
    public static void main(String[] args) {

    }

    public static void testSystemProperties(){
        System.getProperties().keySet().forEach(k->{
            System.out.println(k+"--"+System.getProperty((String) k));
        });
    }

    public static void testArrayCopy(){
        List<Integer> listA = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> listB = Arrays.asList(11, 22, 33, 44, 55);

        Object[] listBArray = listB.toArray();
        System.arraycopy(listA.toArray(),0, listBArray,2,2);
        for (Object o : listBArray) {
            System.out.println(o);
        }
    }


}
