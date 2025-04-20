package com.jiang.singlelearningdemo.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpObjectDecoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class NettyServer {

    private EventLoopGroup bossGroup=new NioEventLoopGroup(1);
    private EventLoopGroup workGroup=new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
    private final Integer port=8888;

    @PostConstruct
    public void init() throws InterruptedException {
        try {
            start();
        }catch (Throwable throwable){
            throw throwable;
        }
    }

    @PreDestroy
    public void destroy(){
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }

    public void start() throws InterruptedException {
        ServerBootstrap serverBootstrap = new ServerBootstrap()
                .group(bossGroup,workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel){
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new HttpServerCodec());//http编解码器
                        pipeline.addLast(new HttpObjectAggregator(65536));//http消息聚合器
                        pipeline.addLast(new ChunkedWriteHandler());//支持异步发送大的码流（例如大文件传输），但不占用过多的内存，防止java内存溢出
                        pipeline.addLast(new WebSocketServerProtocolHandler("/fuck"));
                        pipeline.addLast(new WebSocketHandler());
                    }
                });
            serverBootstrap.bind(port).sync();
        System.out.println("Websocket服务启动---netty---"+port);
    }
}
