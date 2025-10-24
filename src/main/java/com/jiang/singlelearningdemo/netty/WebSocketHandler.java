package com.jiang.singlelearningdemo.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class WebSocketHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    //userId到ChannelHandlerContext的映射
    private final ConcurrentHashMap<String,ChannelHandlerContext> session=new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, WebSocketFrame webSocketFrame) throws Exception {
        if (webSocketFrame instanceof TextWebSocketFrame) {
            String text = ((TextWebSocketFrame) webSocketFrame).text();
        }
    }

    private void sendMsg(ChannelHandlerContext ctx, String msg) {
        ctx.channel().writeAndFlush(new TextWebSocketFrame(msg));
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        String channelId = channelId(ctx);
        String ip = ip(ctx);
//        log.info(ip+"----"+channelId+"连接成功");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        String channelId = channelId(ctx);
        String ip = ip(ctx);
//        log.info(ip+"----"+channelId+"连接已经断开");
    }

    private String channelId(ChannelHandlerContext ctx){
        return ctx.channel().id().asLongText();
    }

    private String ip(ChannelHandlerContext ctx){
        return ctx.channel().remoteAddress().toString();
    }
}
