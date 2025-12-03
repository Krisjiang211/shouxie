package com.jiang.singlelearningdemo.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiang.singlelearningdemo.common.mapper.PartMapper;
import com.jiang.singlelearningdemo.common.pojo.Part;
import com.jiang.singlelearningdemo.common.service.PartService;
import org.springframework.stereotype.Service;

@Service
public class PartServiceImpl extends ServiceImpl<PartMapper, Part>
        implements PartService {
}
