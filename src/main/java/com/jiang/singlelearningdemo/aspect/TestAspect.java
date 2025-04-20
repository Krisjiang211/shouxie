package com.jiang.singlelearningdemo.aspect;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/test/aop")
public class TestAspect {

    @GetMapping("a")
    @Log(desc = "T(String).format('用户id: %s 执行了该方法', #userId)")
    public Boolean ok(@RequestParam Long userId) throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        return true;
    }
}
