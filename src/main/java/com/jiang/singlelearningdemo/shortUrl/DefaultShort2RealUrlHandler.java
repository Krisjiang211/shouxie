package com.jiang.singlelearningdemo.shortUrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class DefaultShort2RealUrlHandler implements ShortUrlToRealUrlHandler {

//    @Autowired
//    private StringRedisTemplate redisTemplate;
//
//    public static final String prefix = "short:url:";
//
//
//    @Override
//    public String swap(String shortUrl) {
//        String key = prefix + shortUrl;
//        return redisTemplate.opsForValue().get(key);
//    }
//
//    @Override
//    public void storeOneKV(String shortUrl, String realUrl) {
//        String key = prefix + shortUrl;
//        if (!realUrl.startsWith("/")) realUrl = "/" + realUrl;
//
//        Boolean res;
//        while (true) {
//            res = redisTemplate.opsForValue().setIfAbsent(key, realUrl, 1, TimeUnit.HOURS);
//            if (Boolean.TRUE.equals(res)) break;
//            key = prefix + idGen();
//        }
//    }

    @Override
    public String swap(String shortUrl) {
        return "";
    }

    @Override
    public void storeOneKV(String shortUrl, String realUrl) {

    }

    @Override
    public String idGen() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
