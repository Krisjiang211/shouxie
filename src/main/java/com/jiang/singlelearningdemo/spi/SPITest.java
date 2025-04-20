package com.jiang.singlelearningdemo.spi;

import com.jiang.demospi.MsgService;

import java.util.ServiceLoader;

public class SPITest {
    public static void main(String[] args) {
        ServiceLoader<MsgService> services= ServiceLoader.load(MsgService.class);
        for (MsgService msgService : services) {
            msgService.saySomething();
        }
    }
}
