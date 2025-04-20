package com.jiang.singlelearningdemo.springFramwork.beans;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class Teacher {
    private String sex;
    private Integer age;


}
