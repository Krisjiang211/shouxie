package com.jiang.singlelearningdemo.nettyMini;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

public class NettyMiniTest {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8082);
        Socket accept = serverSocket.accept();
    }
}
