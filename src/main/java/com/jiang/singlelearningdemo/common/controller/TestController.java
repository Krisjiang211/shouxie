package com.jiang.singlelearningdemo.common.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("test")
public class TestController {

    @PostMapping("ok")
    public Map<String,Object> ok(@RequestBody Map<String,Object> map){
        return map;
    }


}
