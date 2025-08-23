package com.jiang.singlelearningdemo.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PlainUser {

    private String country;

    private String name;

    private Integer age;


    public PlainUser(String country) {
        this.country = country;
    }



}
