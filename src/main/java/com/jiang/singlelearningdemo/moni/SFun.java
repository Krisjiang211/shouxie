package com.jiang.singlelearningdemo.moni;

import java.io.Serializable;
import java.util.function.Function;
@FunctionalInterface
public interface SFun<T,R> extends Serializable, Function<T,R> {
}
