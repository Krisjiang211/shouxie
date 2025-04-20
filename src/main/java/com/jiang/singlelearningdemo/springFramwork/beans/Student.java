package com.jiang.singlelearningdemo.springFramwork.beans;

import com.jiang.singlelearningdemo.springFramwork.annotation.MyComponent;
import lombok.Data;

@MyComponent
@Data
public class Student {
    private String name;
    private Integer age;
}
