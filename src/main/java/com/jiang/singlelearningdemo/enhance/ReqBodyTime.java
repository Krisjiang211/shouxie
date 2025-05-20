package com.jiang.singlelearningdemo.enhance;


import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ReqBodyTime {
}
