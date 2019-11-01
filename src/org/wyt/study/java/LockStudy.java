package org.wyt.study.java;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockStudy {
    public static void main(String[] args) {
        study1();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("------------------------------------------------------");
        study2();
    }

    private static void study1() {
        Resource resource = new Resource();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                resource.produce();
            }
        }, "生产者").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                resource.consume();
            }
        }, "消费者").start();
    }

    private static void study2() {
        ShareData shareData = new ShareData();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareData.show(1);
            }
        }, "a").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareData.show(2);
            }
        }, "b").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareData.show(3);
            }
        }, "c").start();
    }
}

class Resource {
    private int num = 0;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    /**
     * 生产方法
     */
    public void produce() {
        lock.lock();//加锁
        try {
            //多线程中标志位判断统一用while,不用if(if会导致虚假唤醒)
            while (num > 0) {
                condition.await();//阻塞
            }
            num++;
            System.out.println(Thread.currentThread().getName() + " 产品数量：" + num);
            condition.signal();//唤醒等待线程
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();//解锁
        }
    }

    /**
     * 消费方法
     */
    public void consume() {
        lock.lock();//加锁
        try {
            //多线程中标志位判断统一用while,不用if(if会导致虚假唤醒)
            while (num < 1) {
                condition.await();//阻塞
            }
            num--;
            System.out.println(Thread.currentThread().getName() + " 产品数量：" + num);
            condition.signal();//唤醒等待线程
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();//解锁
        }
    }
}

class ShareData {
    private int num = 1; //1 a线程可执行 2 b线程可执行 3 c线程可执行
    private Lock lock = new ReentrantLock();
    private Condition condition1 = lock.newCondition();
    private Condition condition2 = lock.newCondition();
    private Condition condition3 = lock.newCondition();

    public void show(int flag) {
        lock.lock();//加锁
        try {
            if (flag == 1) {
                while (num != 1) {
                    condition1.await();//阻塞
                }
                System.out.println("aaa");
                num = 2;//修改标识
                condition2.signal();//唤醒b
            } else if (flag == 2) {
                while (num != 2) {
                    condition2.await();//阻塞
                }
                System.out.println("bbb");
                num = 3;//修改标识
                condition3.signal();//唤醒c
            } else if (flag == 3) {
                while (num != 3) {
                    condition3.await();//阻塞
                }
                System.out.println("ccc");
                num = 1;//修改标识
                condition1.signal();//唤醒a
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
