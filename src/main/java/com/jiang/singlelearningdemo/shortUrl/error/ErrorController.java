package com.jiang.singlelearningdemo.shortUrl.error;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/error")
@RestController
public class ErrorController {

    @GetMapping("/null/url")
    public String nullUrl(){
        return "你的这个链接不存在";
    }


}
