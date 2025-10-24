package com.jiang.singlelearningdemo;

import jakarta.annotation.PostConstruct;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

@SpringBootApplication
@MapperScan("com.jiang.singlelearningdemo.common.mapper")
public class SingleLearningDemoApplication {

    @Autowired
    ApplicationContext applicationContext;

    public static void main(String[] args) throws InterruptedException {
        SpringApplication app = new SpringApplication(SingleLearningDemoApplication.class);
        app.addBootstrapRegistryInitializer(context->{
            System.out.println("此时项目点击开始 ,是正在启动前的第一步");
        });
        app.addListeners(new ApplicationListener<ApplicationStartingEvent>() {
            @Override
            public void onApplicationEvent(ApplicationStartingEvent event) {
                System.out.println("此时接收到了Springboot刚启动的消息---");
            }
        });
        app.run(args);
//        ConfigurableApplicationContext context = SpringApplication.run(SingleLearningDemoApplication.class, args);
//        System.out.println("configurableApplicationContext.hashCode() = " + context.hashCode());;
//        TimeUnit.SECONDS.sleep(2);
//        context.close();
    }



}
