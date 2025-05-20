package com.jiang.singlelearningdemo.common.controller;

import com.jiang.singlelearningdemo.common.pojo.User;
import com.jiang.singlelearningdemo.common.util.Result;
import com.jiang.singlelearningdemo.enhance.ReqBodyTime;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    ApplicationContext context;

    @PostConstruct
    public void init(){
        System.out.println("ApplicationContext.hashCode() = " + context.hashCode());
    }

    @PostMapping("ok")
    public Map<String,Object> ok(@ReqBodyTime Map<String,Object> map){
        System.out.println("map = " + map);
        return map;
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
}
