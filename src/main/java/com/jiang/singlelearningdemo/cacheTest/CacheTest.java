package com.jiang.singlelearningdemo.cacheTest;

import com.jiang.singlelearningdemo.common.pojo.User;
import com.jiang.singlelearningdemo.common.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("cache")
public class CacheTest {
    @Autowired
    UserService userService;

    @GetMapping("{name}")
    public User getUserByName(@PathVariable String name){
        return userService.getByName(name);
    }
}
