package com.jiang.singlelearningdemo.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

public class NServer {
    private final EventLoopGroup boosGroup =new NioEventLoopGroup(1);
    private final EventLoopGroup workGroup=new NioEventLoopGroup(Runtime.getRuntime().availableProcessors()*2);

    public void init(){
        new ServerBootstrap()
                .group(boosGroup,workGroup)

                .bind(10000);
    }

}
