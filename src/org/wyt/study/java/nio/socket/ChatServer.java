package org.wyt.study.java.nio.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class ChatServer {
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private final int port = 9999;

    public ChatServer(){
        try {
            //得到ServerSocketChannel
            serverSocketChannel = ServerSocketChannel.open();
            //设置是否为阻塞模式
            serverSocketChannel.configureBlocking(false);
            //绑定地址(ip,端口号)
            serverSocketChannel.bind(new InetSocketAddress(port));
            //得到Selector
            selector = Selector.open();
            //将serverSocketChannel注册到selector,指定感兴趣的事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            printInfo("服务器端就绪");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void act(){
        try {
            while (true) {
                if (selector.select(2000) == 0) {
                    /*System.out.println("暂无客户端连接");
                    TimeUnit.SECONDS.sleep(2);*/
                    continue;
                }
                //获取所有的SelectionKey
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    //判断SelectionKey类型
                    if (selectionKey.isAcceptable()) {//连接事件
                        //获取客户端连接通道
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        //获取客户端的地址
                        SocketAddress remoteAddress = socketChannel.getRemoteAddress();
                        printInfo("客户端：" + remoteAddress.toString() + "连接");
                        //设置为非阻塞模式
                        socketChannel.configureBlocking(false);
                        //绑定读取事件，用来获得客户端发来的内容
                        socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    }
                    if (selectionKey.isReadable()) {//读取事件
                        readMessage(selectionKey);
                    }
                    iterator.remove();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //读取消息
    public void readMessage(SelectionKey selectionKey) throws IOException {
        //获取SocketChannel
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        //设置为非阻塞模式
        socketChannel.configureBlocking(false);
        //获取客户端的地址
        SocketAddress remoteAddress = socketChannel.getRemoteAddress();
        //获取传来的数据
        ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
        int read = 0;
        try {
            read = socketChannel.read(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
            socketChannel.close();
        }

        if (read > 0) {
            String message = new String(byteBuffer.array());
            printInfo("收到客户端：" + remoteAddress.toString() + "发来的信息：" + message);
            broadcast(socketChannel, message);
        }
        if(read == -1){
            printInfo("客户端：" + remoteAddress.toString() + "断开连接");
        }
    }

    //向其他客户端广播消息
    private void broadcast(SocketChannel sourceChannel, String message) throws IOException {
        //发送给其他客户端
        Set<SelectionKey> keys = selector.keys();//获取所有的SelectionKey
        for (SelectionKey selectionKey1 : keys) {
            SelectableChannel channel = selectionKey1.channel();
            System.out.println(channel.getClass().getName());
            if (channel instanceof SocketChannel) {
                SocketChannel targetChannel = (SocketChannel) channel;
                if(targetChannel != sourceChannel) {
                    System.out.println("广播消息");
                    //向其他客户端发消息
                    String msg = message + "[发送者:" + sourceChannel.getRemoteAddress().toString() + "]";
                    targetChannel.write(ByteBuffer.wrap(msg.getBytes()));
                }
            }
        }
    }

    private void printInfo(String str){
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        System.out.println("[" + dateFormat.format(new Date()) + "] " + str);
    }

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        chatServer.act();
    }
}
