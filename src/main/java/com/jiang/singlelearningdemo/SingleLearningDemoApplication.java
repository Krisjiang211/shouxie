package com.jiang.singlelearningdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.function.Function;

@SpringBootApplication
@MapperScan("com.jiang.singlelearningdemo.common.mapper")
public class SingleLearningDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SingleLearningDemoApplication.class, args);
    }

}
