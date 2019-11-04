package org.wyt.study.java;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程简单工作流程
 * 1.创建后等待任务
 * 2.接到一个任务：
    2.1.当正在运行的线程数 < corePoolSize时，直接执行任务
    2.2.当正在运行的线程数 >= corePoolSize时，任务会进入等待队列
    2.3.当等待队列满并且正在运行的线程数 < 最大线程数时，会扩容创建非核心线程执行这个任务
    2.4.当等待队列满并且正在运行的线程数 >= 最大线程数时，执行拒绝策略
 * 3.当一个线程执行完成后，会从等待队列取任务来执行
 * 4.当一个线程空闲时间 > keepAliveTime时，多余的线程会被销毁，只留下corePoolSize时个线程
 */
public class ThreadPoolStudy {

    public static void main(String[] args) {
        //defaultThreadPool();
        customThreadPool();
    }

    /**
     * 线程池参数
     * int corePoolSize 核心线程数(最小线程数)
     * int maximumPoolSize 最大线程数
     * long keepAliveTime 线程最大空闲时间，
                            当线程池中的线程数 > corePoolSize是时，如果线程的空闲时间 >= keepAliveTime,
                            多余的线程会被销毁，直到剩下corePoolSize个线程
     * TimeUnit unit 时间单位
     * BlockingQueue<Runnable> workQueue 阻塞队列
     * ThreadFactory threadFactory 生成线程的工厂类，一般用默认即可
     * RejectedExecutionHandler handler 拒绝策略，jdk自带的有如下几种
                            1.AbortPolicy(默认) 抛出RejectedExecutionException异常
                            2.CallerRunsPolicy 将多余的任务退回给调用者，不会抛出异常
                            3.DiscardOldestPolicy 扔掉等待队列等待最久的任务，然后再次尝试提交本次任务，不会抛出异常
                            4.DiscardPolicy 直接抛弃本次任务，不会抛出异常
     */
    private static void customThreadPool() {
        //ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0, 5, 2L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(3), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0, 5, 2L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(3), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
        //ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0, 5, 2L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(3), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
        //ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0, 5, 2L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(3), Executors.defaultThreadFactory(), new ThreadPoolExecutor.DiscardOldestPolicy());
        //ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0, 5, 2L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(3), Executors.defaultThreadFactory(), new ThreadPoolExecutor.DiscardPolicy());

        try {
            for (int i = 0; i < 10; i++) {
                final int temp = i;
                threadPoolExecutor.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + "\t 执行任务" + temp);
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPoolExecutor.shutdown();//销毁线程池
        }
    }

    /**
     * jdk内置ThreadPool
     * Executors.newFixedThreadPool() 固定大小的ThreadPool，LinkedBlockingQueue 的默认最大值为Integer.MAX_VALUE, 可能导致oom
     * Executors.newSingleThreadExecutor() 单个线程的ThreadPool，LinkedBlockingQueue 的默认最大值为Integer.MAX_VALUE, 可能导致oom
     * Executors.newCachedThreadPool() 可扩容的ThreadPool
     * <p>
     * (重要)在生产中要使用自定义的线程池
     */
    private static void defaultThreadPool() {
        //ExecutorService executorService = Executors.newFixedThreadPool(5);
        //ExecutorService executorService = Executors.newSingleThreadExecutor();
        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < 3; i++) {
            final int temp = i;
            executorService.submit(() -> {
                System.out.println(Thread.currentThread().getName() + "任务" + temp + "执行");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (Exception e) {
                }
            });
        }
        executorService.shutdown();
    }
}
