package com.jiang.singlelearningdemo.BFS.testEntity;

import com.jiang.singlelearningdemo.BFS.Dict;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class A {
    private B b;
    @Dict(dicCode = "test_dict")
    private String a2;
    private C c;
    private List<C> cs;
    private List<B> bs;

    public static A getInstance(){
        A a = new A();
        C c = new C("c1", "c2");
        B b = new B("b1","b2",c);
        a.setA2("a2");
        a.setB(b);
        a.setC(c);
        a.setCs(Arrays.asList(c,c));
        a.setBs(Arrays.asList(b,b));
        return a;
    }
    private A(){}

}
/**
 * {
 *     {
 *         "b1" : "xxx",
 *         "b2" : "xxx",
 *         "b2DictParse" : "xxx",
 *         {
 *             "c1" : "xxx",
 *             "c2" : "xxx",
 *             "c2DictParse" : "xxx",
 *         }
 *     }
 *     "a2" : "xxx",
 *     "a2DictParse" : "xxx",
 *     {
 *         "c1" : "xxx",
 *         "c2" : "xxx",
 *         "c2DictParse" : "xxx",
 *     }
 * }
 */
