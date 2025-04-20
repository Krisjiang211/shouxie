package com.jiang.singlelearningdemo.iterableTest;

import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.Iterator;

public class User implements Iterable<String> {

    String name;
    Integer age;

    public User(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public Iterator<String> iterator() {
        return new UserIterator();
    }

    /**
     * 非静态内部类会默认持有外部类实例的引用
     * 可以使用User.this获取到外部类的实例
     * 然后如果要是内部类自己访问自己的属性,直接用this就可以
     */
    class UserIterator implements Iterator<String>{
        Class<User> mainClass=User.class;
        int cursor=0;
        int length = mainClass.getDeclaredFields().length;
        Field[] fields = mainClass.getDeclaredFields();
        public UserIterator() {
            for (Field field : fields) {
                field.setAccessible(true);
            }
        }
        @Override
        public boolean hasNext() {
            return cursor<length;
        }

        @SneakyThrows
        @Override
        public String next() {
            Field field = fields[cursor];
            String k = field.getName();
            Object v = field.get(User.this);
            cursor++;
            return k+" : "+v;
        }
    }
}
