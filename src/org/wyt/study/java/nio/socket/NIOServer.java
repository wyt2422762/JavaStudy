package org.wyt.study.java.nio.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOServer {

    public static void main(String[] args) throws IOException {

        //得到ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //设置是否为阻塞模式
        serverSocketChannel.configureBlocking(false);
        //绑定地址(ip,端口号)
        serverSocketChannel.bind(new InetSocketAddress(9999));
        //得到Selector
        Selector selector = Selector.open();
        //将serverSocketChannel注册到selectpr,指定感兴趣的事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            //监听事件
            if (selector.select(2000) == 0) {
                //System.out.println("暂无事件");
                continue;
            }

            //获得selector监听中的事件
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey skey = iterator.next();
                if (skey.isAcceptable()) {
                    System.out.println("事件类型：OP_ACCEPT");
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                if (skey.isReadable()) {
                    System.out.println("事件类型：OP_READ");
                    SocketChannel socketChannel = (SocketChannel) skey.channel();
                    socketChannel.configureBlocking(false);
                    ByteBuffer byteBuffer = (ByteBuffer) skey.attachment();
                    int read = socketChannel.read(byteBuffer);
                    while(read != -1){
                        byteBuffer.flip();//将buffer的position致为0
                        //读取buffer中的内容
                        while(byteBuffer.hasRemaining()) {
                            System.out.print((char) byteBuffer.get());
                        }
                        //清空buffer
                        byteBuffer.clear();
                        //继续读取
                        read = socketChannel.read(byteBuffer);
                    }
                }
                iterator.remove();
            }
        }
    }
}
