package com.jiang.singlelearningdemo.proxy.core;

import com.jiang.singlelearningdemo.proxy.core.annotation.Param;
import com.jiang.singlelearningdemo.proxy.core.pojo.User;

public interface UserMapper {

    User selectById(@Param("id") Long id);
}
