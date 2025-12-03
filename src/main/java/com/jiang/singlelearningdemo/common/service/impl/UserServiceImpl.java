package com.jiang.singlelearningdemo.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiang.singlelearningdemo.common.mapper.UserMapper;
import com.jiang.singlelearningdemo.common.pojo.User;
import com.jiang.singlelearningdemo.common.service.UserService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
* @author 86136
* @description 针对表【user】的数据库操作Service实现
* @createDate 2025-03-28 18:19:35
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{




    @Override
    @Cacheable(value = "user", key = "#name")
    public User getByName(String name) {
        return lambdaQuery()
                .eq(User::getName, name)
                .list()
                .get(0);

    }


    @Override
    public User mybatisTest(){
        return lambdaQuery()
                .eq(User::getName,"kris")
                .one();
    }
}




