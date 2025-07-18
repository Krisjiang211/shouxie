package com.jiang.singlelearningdemo.pvz2;

public class Item<T> {
    private T obj;
    private final long ms;//存入的时候的时间, 不可变

    public Item(T obj, long ms) {
        this.obj = obj;
        this.ms = ms;
    }

    public long getMs() {
        return ms;
    }

}
