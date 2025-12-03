package com.jiang.singlelearningdemo.common.controller;

import com.jiang.singlelearningdemo.common.pojo.User;
import com.jiang.singlelearningdemo.common.service.UserService;
import com.jiang.singlelearningdemo.common.util.Result;
import com.jiang.singlelearningdemo.enhance.ReqBodyTime;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    ApplicationContext context;

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    UserService userService;

    @GetMapping
    public User get(){
        return userService.mybatisTest();
    }

    @PostConstruct
    public void init(){
        System.out.println("ApplicationContext.hashCode() = " + context.hashCode());
    }

    List<String> xingList =List.of("张","李","王","赵","钱","周","吴","郑","孔");
    List<String> mingList =List.of("三","四","五","六","七","八","九","十","十一");
    List<String> nameList =List.of("豪","宝","杰","强","亮","天","时","事","下");

    @GetMapping("test/get")
    public User testGet(){
        User user = userService.getById(1);
        return user;
    }

    @GetMapping("one")
    public User testGeById(@RequestParam Long id){
        return userService.getById(id);
    }


    @GetMapping("test/get/all")
    public List<User> testGetAll(){
        return userService.list();
    }

    @GetMapping("test/insert")
    public String testInsert(){
        User user = new User();
        user.setName(getRandomName());
        user.setAge((int)(Math.random()*100));
        userService.save(user);
        return "success";
    }
    private String getRandomName(){
        int xingIndex = (int)(Math.random()*xingList.size());
        int mingIndex = (int)(Math.random()*mingList.size());
        int nameIndex = (int)(Math.random()*nameList.size());
        return xingList.get(xingIndex)+mingList.get(mingIndex)+nameList.get(nameIndex);
    }



    @PostMapping("ok")
    public Map<String,Object> ok(@ReqBodyTime Map<String,Object> map){
        System.out.println("map = " + map);
        return map;
    }


    @PostMapping("test/user")
    public User testUser(@RequestBody User user){

        System.out.println("user = " + user);
        return user;
    }




    @GetMapping("1")
    public String ok1(HttpServletRequest request){
        return request.toString();
    }
    @GetMapping("2")
    public Result<User> ok2(HttpServletRequest request){
        User user = new User();
        user.setName("张三");
        user.setAge(18);
        return Result.ok(user);
    }


    @GetMapping("/async/data")
    public DeferredResult<String> getAsyncData() {
        DeferredResult<String> deferredResult = new DeferredResult<>();

        // 模拟异步操作（如数据库查询、RPC调用）
        new Thread(() -> {
            try {
                Thread.sleep(1000); // 模拟耗时操作
                deferredResult.setResult("Async data loaded!");
            } catch (Exception e) {
                deferredResult.setErrorResult(e);
            }
        }).start();

        return deferredResult;
    }

    @GetMapping("stream/resEmiter")
    public ResponseBodyEmitter streamData(HttpServletResponse response) {
        response.setHeader("Transfer-Encoding", "chunked");
        response.setHeader("Content-Type", "text/html;charset=UTF-8");

        ResponseBodyEmitter emitter = new ResponseBodyEmitter();

        new Thread(() -> {
            try {
                for (int i = 1; i <= 100; i++) {
                    emitter.send("Chunk " + i + "\n"); // 发送数据块

                    Thread.sleep(200); // 模拟延迟
                }
                emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        }).start();

        return emitter;
    }

    public static void main(String[] args) {
        Consumer<String> consumer1= System.out::println;
        Consumer<String> consumer2= System.out::println;
        consumer1.andThen(consumer2).andThen(consumer1).accept("hello");
    }
}
