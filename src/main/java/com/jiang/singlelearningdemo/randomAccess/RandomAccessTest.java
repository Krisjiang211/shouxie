package com.jiang.singlelearningdemo.randomAccess;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomAccessTest {

    public static void main(String[] args) throws FileNotFoundException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(new File("aaa.text"),"rw");
        try(randomAccessFile) {
            randomAccessFile.seek(3);
            System.out.println("int返回值的结果randomAccessFile.read() = " + randomAccessFile.read());
            System.out.println("randomAccessFile.read() = " + (char)randomAccessFile.read());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
