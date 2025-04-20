package com.jiang.singlelearningdemo.iterableTest;

/**
 * 这里是用来介绍非静态内部类的属性访问的Demo
 */
public class Outer {
    private String outerField = "外部类字段";

    private Inner inner;

    public Outer() {
        inner=new Inner();
    }

    public class Inner {
        private String innerField = "内部类字段";

        public void print() {
            System.out.println(this.innerField);    // Inner.this.innerField 简写
            System.out.println(Outer.this.outerField);  // 明确访问 Outer 的 outerField
        }
    }

    public static void main(String[] args) {
        Outer outer = new Outer();
        outer.inner.print();
    }
}

