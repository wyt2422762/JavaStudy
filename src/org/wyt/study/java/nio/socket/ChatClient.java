package org.wyt.study.java.nio.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class ChatClient {

    private SocketChannel socketChannel;
    private final String host = "127.0.0.1";//服务端ip
    private final int port = 9999;//服务端端口

    public ChatClient(){
        try {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            boolean connected = socketChannel.connect(new InetSocketAddress(host, port));
            if(!connected){
                while (!socketChannel.finishConnect()) {
                    System.out.println("持续连接服务端");
                }
            }
            String userName = getUserName();
            printInfo("客户端：" + userName + "连接成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message){
        try {
            socketChannel.write(ByteBuffer.wrap(message.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveMessage(){
        try {
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int read = socketChannel.read(byteBuffer);
            StringBuilder msg = new StringBuilder();
            while(read > 0){
                String message = new String(byteBuffer.array()).trim();
                msg.append(message);
                //清空buffer
                byteBuffer.clear();
                read = socketChannel.read(byteBuffer);
            }
            if(msg.length() > 0) {
                printInfo("收到服务端广播的消息：" + msg.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getUserName(){
        try {
            return socketChannel.getLocalAddress().toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void printInfo(String str){
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        System.out.println("[" + dateFormat.format(new Date()) + "] " + str);
    }

    public static void main(String[] args) {
        ChatClient chatClient = new ChatClient();

        //接收消息线程
        new Thread(() -> {
            while(true) {
                chatClient.receiveMessage();
            }
        }).start();

        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNextLine()){
            String msg = scanner.nextLine();
            chatClient.sendMessage(msg);
        }
    }
}
