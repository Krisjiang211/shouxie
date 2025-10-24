package com.jiang.singlelearningdemo.common.pojo;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Data
@ToString
@AllArgsConstructor
@Builder
@Jacksonized
public class PlainUser {

    private String country;

    private String name;

    private Integer age;


    public PlainUser(String country) {
        this.country = country;
    }



}
