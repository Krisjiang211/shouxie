package com.jiang.singlelearningdemo.proxy.core.pojo;

import com.jiang.singlelearningdemo.proxy.core.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "user")
public class User {
    private long id;
    private String name;
    private int age;
}
