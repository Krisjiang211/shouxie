package com.jiang.singlelearningdemo.unsafeThread;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleDateFormatTest {

    public static String FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static void main(String[] args) {
        String format = new SimpleDateFormat(FORMAT).format(new Date());
        System.out.println("format = " + format);
    }
}
