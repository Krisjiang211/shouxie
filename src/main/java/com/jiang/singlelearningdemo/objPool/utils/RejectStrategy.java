package com.jiang.singlelearningdemo.objPool.utils;

import com.jiang.singlelearningdemo.objPool.ObjPool;
import com.jiang.singlelearningdemo.objPool.domain.NeedNewFlag;

import java.lang.reflect.Constructor;

public interface RejectStrategy<T> {

    /**
     * @param flag 是个对象, 有两个方法.
     *             如果选择iNeed()方法,那么我们会为您自动创建一个新的对象并返回
     *             如果选择iDontNeed(),那么我们不会为您创建. 我们将会返回您操作之后的返回值ObjPool.Obj<T>
     * @param constructor 原始对象的构造器
     * @param args 原始对象的构造器对应的所有初始参数值
     */
    ObjPool.Obj<T> reject(NeedNewFlag flag, Constructor<T> constructor, Object[] args);

}
