package org.wyt.study.java.nio.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {

    public static void main(String[] args) throws IOException {

        //得到SocketChannel
        SocketChannel socketChannel = SocketChannel.open();
        //设置是否为阻塞模式
        socketChannel.configureBlocking(false);
        //连接server
        boolean connected = socketChannel.connect(new InetSocketAddress(9999));
        if(!connected){
            while(!socketChannel.finishConnect()){
                System.out.println("连接服务器端");
            }

            ByteBuffer byteBuffer = ByteBuffer.wrap("hello, Server".getBytes());
            socketChannel.write(byteBuffer);

            System.out.println("发送事件完毕");

            while(true){

            }
        }

    }
}
