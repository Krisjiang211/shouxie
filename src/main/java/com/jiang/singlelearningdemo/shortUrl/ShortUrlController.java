package com.jiang.singlelearningdemo.shortUrl;

import com.jiang.singlelearningdemo.shortUrl.entity.ShortUrlStoreDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/s")
public class ShortUrlController {

    @Autowired
    private ShortUrlToRealUrlHandler shortUrlToRealUrlHandler;

    @GetMapping("/{shortUrl}")
    public String getRealUrl(@PathVariable String shortUrl) {
        String realUrl = shortUrlToRealUrlHandler.swap(shortUrl);
        if (realUrl == null) {
            return "forward:/error/null/url";
        }
        return "forward:"+realUrl;
    }


    /**
     *测试:
     * http://localhost:1314/s/store
     * {
     *    "shortUrl":"abc",
     *    "realUrl":"/test/one?id=1"
     * }
     *
     */
    //存入一个kv映射
    @PostMapping("store")
    @ResponseBody
    public String store(@RequestBody ShortUrlStoreDto dto) {
        if (dto.getShortUrl()==null) {
            String genShortUrl = shortUrlToRealUrlHandler.idGen();
            dto.setShortUrl(genShortUrl);
        }
        shortUrlToRealUrlHandler.storeOneKV(dto.getShortUrl(), dto.getRealUrl());
        String okShortUrl = "http://localhost:1314/s/"+dto.getShortUrl();
        return "您的短链接为: { "+ okShortUrl + "} \n有效期为1小时, 请尽快使用";
    }

}
