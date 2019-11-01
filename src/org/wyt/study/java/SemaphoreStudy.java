package org.wyt.study.java;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreStudy {

    public static void main(String[] args) {
        semaphore();
    }

    private static void semaphore() {
        /*
         Semaphore(int permits)
         permits: 资源数量
        */
        Semaphore semaphore = new Semaphore(3);
        for (int i = 1; i <= 7; i++) {
            new Thread(() -> {
                try {
                    semaphore.acquire();//占用资源
                    System.out.println("线程" + Thread.currentThread().getName() + "占用资源");
                    TimeUnit.SECONDS.sleep(5);
                    System.out.println("线程" + Thread.currentThread().getName() + "占用资源5秒后释放资源");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();//释放资源
                }
            }, "t" + i).start();
        }
    }
}
