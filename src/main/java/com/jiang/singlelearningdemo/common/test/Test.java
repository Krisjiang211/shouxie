package com.jiang.singlelearningdemo.common.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiang.singlelearningdemo.common.pojo.PlainUser;
import lombok.SneakyThrows;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


public class Test {


    @SneakyThrows
    public static void main(String[] args) {
        Test test = new Test();
        ExecutorService pool = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 100; i++) {
            pool.submit(test::testConcurrentHashMap);
        }
        System.out.println("test.inTimes = " + test.inTimes);;

    }


    AtomicInteger inTimes = new AtomicInteger(0);
    ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();
    public void testConcurrentHashMap(){
        if (null == map.get("1")) {
            map.put("1","3");
            inTimes.incrementAndGet();//进来一次那么就＋1, 我们的期望是这个值是1 ,也就是只被put了一次
        }

        Object put = map.put("1", "3");
        if (put != null) {//说明是覆盖(也就是这个地方其实早就有值了)
            map.put("1",put);//重新再把原来的值插回去
        }else {
            //原值为空, 可以继续向下执行
            //.....
        }

    }

    @SneakyThrows
    public void testJacksonizedAnnotation(){
        ObjectMapper objectMapper = new ObjectMapper();
        PlainUser obj = PlainUser.builder()
                .age(1)
                .name("kris")
                .country("CN").build();
        String jsonStr = objectMapper.writeValueAsString(obj);
        PlainUser newObj = objectMapper.readValue(jsonStr, PlainUser.class);
        System.out.println(jsonStr);
        System.out.println(newObj);
    }

}
