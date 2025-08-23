package com.jiang.singlelearningdemo.objPool.test;

import com.jiang.singlelearningdemo.common.pojo.PlainUser;
import com.jiang.singlelearningdemo.objPool.utils.GiveBackStrategy;

public class PlainUserGivebackStrategy implements GiveBackStrategy<PlainUser> {
    @Override
    public void process(PlainUser plainUser) {
        plainUser.setAge(null);
        plainUser.setName(null);
    }
}
