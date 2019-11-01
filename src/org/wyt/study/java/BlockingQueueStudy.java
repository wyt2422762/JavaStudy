package org.wyt.study.java;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * BlockingQueue 阻塞队列
 * 主要有7种实现类
 * ArrayBlockingQueue(重点) 数组形式的有界阻塞队列
 * LinkedBlockingQueue(重点) 链表形式的有界(Integer.MAX_VALUE)阻塞队列
 * PriorityBlockingQueue 可以优先级排序的无界阻塞队列
 * DelayQueue 使用优先级队列实现的延迟无界阻塞队列
 * SynchronousQueue(重点) 单个元素的阻塞队列
 * LinkedBlockingDeque 链表形式的无界阻塞队列
 * LinkedTransferQueue 链表形式的双向阻塞队列
 *
 * 方法
 * 添加元素                         删除元素                     获取队首元素
 * boolean add(e)                   E remove()                   E element()    该组方法操作失败会直接跑出异常
 * boolean offer(e)                 E poll()                     E peek()       该组方法操作失败返回false 或者 NULL
 * void put(e)                      E take()                                    该组方法会阻塞，直到成功或者中断
 * boolean offer(e,time,timeUnit)   E poll(time, timeUnit)                      该组方法会阻塞一段时间，失败会返回false 或者 NULL
 */
public class BlockingQueueStudy {
    public static void main(String[] args) {
        //simpleTest();
        producerAndConsumer();
    }

    private static void producerAndConsumer() {
        ProduceeAndConsumer produceeAndConsumer = new ProduceeAndConsumer(new ArrayBlockingQueue<String>(10));
        new Thread(()->{
            try {
                produceeAndConsumer.produce();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "producer1").start();
        new Thread(()->{
            try {
                produceeAndConsumer.produce();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "producer2").start();
        new Thread(()->{
            try {
                produceeAndConsumer.consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "consumer").start();

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("------------------------------------------");
        produceeAndConsumer.stop();
    }

    private static void simpleTest() throws InterruptedException {
        BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue(3);
        blockingQueue.add(1);
        blockingQueue.add(1);
        blockingQueue.add(1);
        //blockingQueue.add(1);
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());
        //System.out.println(blockingQueue.remove());
    }
}

class ProduceeAndConsumer{
    private volatile boolean flag = true;//标识位
    private AtomicInteger num = new AtomicInteger();//原子整型
    private BlockingQueue<String> blockingQueue;

    public ProduceeAndConsumer(BlockingQueue<String> blockingQueue){
        this.blockingQueue = blockingQueue;
        System.out.println(blockingQueue.getClass().getName());
    }

    public void produce() throws InterruptedException {
        boolean returnVal;
        String product;
        while(flag){
            product = num.incrementAndGet()+"";
            returnVal = blockingQueue.offer(product, 2, TimeUnit.SECONDS);
            if(returnVal){
                System.out.println(Thread.currentThread().getName() + ": 向阻塞队列插入数据成功 " + product);
            }else{
                System.out.println(Thread.currentThread().getName() + ": 向阻塞队列插入数据失败");
            }
            TimeUnit.SECONDS.sleep(2);
        }
        System.out.println(Thread.currentThread().getName() + ": 向阻塞队列插入数据操作停止");
    }

    public void consume() throws InterruptedException {
        String returnVal;
        while(flag){
            returnVal = blockingQueue.poll(2, TimeUnit.SECONDS);
            if(returnVal != null){
                System.out.println(Thread.currentThread().getName() + ": 从阻塞队列获取数据成功 " + returnVal);
            }else{
                System.out.println(Thread.currentThread().getName() + ": 从阻塞队列获取数据失败");
                flag = false;
                return;
            }
            TimeUnit.SECONDS.sleep(2);
        }
        System.out.println(Thread.currentThread().getName() + ": 从阻塞队列获取数据操作停止");
    }

    public void stop(){
        flag = false;
    }

}
