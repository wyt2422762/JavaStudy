package org.wyt.study.java;

import java.util.concurrent.TimeUnit;

/**
 * java 自旋锁 AtomicReference（CAS+自旋锁）
 */
public class SelfSpin {
    public static void main(String[] args) {
        SelfSpinLock spl = new SelfSpinLock();
        new Thread(() -> {
            spl.spin();
        }, "t1").start();
        new Thread(() -> {
            spl.spin();
        }, "t2").start();
        new Thread(() -> {
            spl.spin();
        }, "t3").start();
    }
}

class SelfSpinLock {
    private volatile boolean flag = true;

    public boolean getLock() {
        if (flag) {
            flag = false;
            return true;
        } else {
            return false;
        }
    }

    public void releaseLock() {
        flag = true;
    }

    public void spin() {
        while (!getLock()) {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程" + Thread.currentThread().getName() + "循环等待");
        }
        System.out.println("线程" + Thread.currentThread().getName() + "开始执行操作");
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("线程" + Thread.currentThread().getName() + "执行操作结束");
        releaseLock();
    }
}