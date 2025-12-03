package com.jiang.singlelearningdemo.shortUrl.entity;

import lombok.Data;

@Data
public class ShortUrlStoreDto {
    private String shortUrl;
    private String realUrl;//真实url, 可以附带参数拼接
}
