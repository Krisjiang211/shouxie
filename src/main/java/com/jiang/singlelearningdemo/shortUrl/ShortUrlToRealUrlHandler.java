package com.jiang.singlelearningdemo.shortUrl;

public interface ShortUrlToRealUrlHandler {
    //短链接 转化成 真实链接
    String swap(String shortUrl);

    //存入一个映射关系
    void storeOneKV(String shortUrl,String realUrl);

    String idGen();

}
