package org.wyt.study.java.nio.file;

import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * NIO文件操作
 * 1.写文件步骤：1.得到channel 3.构建缓冲区Buffer 4.向缓冲区写入数据 5.将buffer中的数据写入文件 6.关闭
 * 2.读文件步骤：1.得到channel 3.构建缓冲区Buffer 4.向缓冲区写入数据 5.循环读取buffer中的内容   6.关闭
 */
public class FileNioTest {

    public static void main(String[] args) {
        //writeToFile("writeToFile.txt", "hello, nio");
        //readFromFile("writeToFile.txt");
        copyFile("writeToFile.txt", "copyFile.txt");
    }

    /**
     * NIO方式写文件
     * @param filePath
     */
    public static void writeToFile(String filePath, String data){
        try(FileOutputStream fos = new FileOutputStream(filePath)) {
            //文件通道
            FileChannel channel = fos.getChannel();
            //缓冲区，初始大小1024
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            //向Buffer里写入数据
            buffer.put(data.getBytes());
            //重置buffer position
            buffer.flip();
            //向文件写入数据
            channel.write(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * NIO方式读文件
     * @param filePath
     */
    public static void readFromFile(String filePath) {
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(filePath, "r")){
            FileChannel channel = randomAccessFile.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int byteReaded = channel.read(byteBuffer);
            while(byteReaded != -1){//
                byteBuffer.flip();//将buffer的position致为0
                //读取buffer中的内容
                while(byteBuffer.hasRemaining()) {
                    System.out.print((char) byteBuffer.get());
                }
                //清空buffer
                byteBuffer.clear();
                //继续读取
                byteReaded = channel.read(byteBuffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * NIO方式copy文件内容
     * @param sourceFile
     * @param destFile
     */
    public static void copyFile(String sourceFile, String destFile){
        try(RandomAccessFile sRandomAccessFile = new RandomAccessFile(sourceFile, "r");
            RandomAccessFile dRandomAccessFile = new RandomAccessFile(destFile, "rw")){

            FileChannel sourceChannel = sRandomAccessFile.getChannel();
            FileChannel destChannel = dRandomAccessFile.getChannel();

            //copy通道中的内容
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
