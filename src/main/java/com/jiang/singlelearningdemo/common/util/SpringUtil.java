package com.jiang.singlelearningdemo.common.util;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;
@Slf4j
public class SpringUtil {
    private static ApplicationContext applicationContext = null;

    private static AtomicBoolean canInit = new AtomicBoolean(true);


    public static void setApplicationContext(ApplicationContext applicationContext) {
        if (canInit.compareAndSet(true,false)){
            SpringUtil.applicationContext = applicationContext;
            return;
        }
        log.error("ApplicationContext已经被初始化了, 请不要强制初始化");
    }
    public static void getBean(String beanName){
        applicationContext.getBean(beanName);
    }

    public static <T> T getBean(Class<T> clazz){
        return applicationContext.getBean(clazz);
    }
}

@Component
class InitializeSpringUtil{
    @Autowired
    private ApplicationContext applicationContext;
    @PostConstruct
    public void init(){
        SpringUtil.setApplicationContext(applicationContext);
    }
}
