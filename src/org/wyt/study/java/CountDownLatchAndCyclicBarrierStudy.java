package org.wyt.study.java;


import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * CountDownLatch 一些线程阻塞到其他线程全部执行完毕(计数器减为0)后才会被唤醒
 * CyclicBarrier N个线程到达屏障点后被阻塞，直到足够多的线程都到达屏障点后才会被唤醒
 * <p>
 * CountDownLatch是线程组之间的等待，即一个(或多个)线程等待N个线程完成某件事情之后再执行；
 * CyclicBarrier则是线程组内的等待，即每个线程相互等待，即N个线程都被拦截之后，然后依次执行。
 * CountDownLatch是减计数方式，而CyclicBarrier是加计数方式。
 * CountDownLatch计数为0无法重置，无法复用；CyclicBarrier计数达到初始值，则可以用reset()重置，可以复用。
 */
public class CountDownLatchAndCyclicBarrierStudy {

    public static void main(String[] args) throws InterruptedException {
        countDownLatchStudy();
        TimeUnit.SECONDS.sleep(5);
        System.out.println("----------------------------------------------------");
        cyclicBarrierStudy1();
        TimeUnit.SECONDS.sleep(5);
        System.out.println("----------------------------------------------------");
        cyclicBarrierStudy2();
    }

    private static void countDownLatchStudy() throws InterruptedException {
        /*
          CyclicBarrier(int parties, Runnable barrierAction)
          parties: 需要到达屏障线程数量
         */
        CountDownLatch countDownLatch = new CountDownLatch(7);
        for (int i = 1; i <= 8; i++) {
            new Thread(() -> {
                System.out.println("线程" + Thread.currentThread().getName() + "执行");
                countDownLatch.countDown();//计数器减一
            }, "t" + i).start();
        }
        countDownLatch.await();//阻塞其他线程直到计数器减为0
        System.out.println("等待线程执行");
    }

    private static void cyclicBarrierStudy1() {
        /*
          CyclicBarrier(int parties, Runnable barrierAction)
          parties: 需要到达屏障线程数量
          barrierAction: 到达屏障点的线程数量达标后执行的操作，由最后一个到达的线程负责执行
         */
        CyclicBarrier cyclicBarrier = new CyclicBarrier(7, () -> System.out.println(Thread.currentThread().getName() + "说：让我们摆放龙珠召唤神龙吧"));
        for (int i = 1; i <= 7; i++) {
            final int tempNum = i;
            new Thread(() -> {
                try {
                    System.out.println("收集到第" + tempNum + "颗龙珠");
                    cyclicBarrier.await();//到达屏障点，阻塞
                    System.out.println("放置第" + tempNum + "颗龙珠");
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }, "t" + i).start();
        }
    }

    private static void cyclicBarrierStudy2() {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(7);
        for (int i = 1; i <= 7; i++) {
            final int tempNum = i;
            new Thread(() -> {
                try {
                    System.out.println("人员" + tempNum + "到达");
                    cyclicBarrier.await();//到达屏障点，阻塞
                    System.out.println("人员" + tempNum + "准备开会");
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }, "t" + i).start();
        }
    }

}
