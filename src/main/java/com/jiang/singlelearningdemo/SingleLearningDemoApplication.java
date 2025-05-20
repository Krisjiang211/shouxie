package com.jiang.singlelearningdemo;

import jakarta.annotation.PostConstruct;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@SpringBootApplication
@MapperScan("com.jiang.singlelearningdemo.common.mapper")
public class SingleLearningDemoApplication {

    @Autowired
    ApplicationContext applicationContext;

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(SingleLearningDemoApplication.class, args);
//        System.out.println("configurableApplicationContext.hashCode() = " + context.hashCode());;
//        TimeUnit.SECONDS.sleep(2);
//        context.close();
    }



}
