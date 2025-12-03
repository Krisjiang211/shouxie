package com.jiang.singlelearningdemo.common.controller;

import com.jiang.singlelearningdemo.common.mapper.PartMapper;
import com.jiang.singlelearningdemo.common.pojo.Part;
import com.jiang.singlelearningdemo.common.service.PartService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("test/dynamic/source")
public class DynamicDataSourceController {

    @Resource
    private PartMapper partMapper;

    @GetMapping
    public List<Part> get(){
        List<Part> all = partMapper.getAll();
        return all;
    }


}
