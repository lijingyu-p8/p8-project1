package com.lijingyu.threadtest.thread02.lock;

import java.util.concurrent.Semaphore;

public class MySemaphore {
    public static void main(String[] args) {
        //设置同时可以有几个线程一起运行，并且第二个参数可以设置是否是公平锁。
        Semaphore semaphore = new Semaphore(1, false);
        new Thread(() -> {
            try {
                //获得锁，并且将设置的可同时运行线程数-1  1->0
                semaphore.acquire();
                System.out.println("T111111111");
                Thread.sleep(2000);
                System.out.println("T111111111");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                //使用完毕，释放锁。+1  0->1
                semaphore.release();
            }
        }).start();
        new Thread(() -> {
            try {
                //获得锁，并且将设置的可同时运行线程数-1  1->0
                semaphore.acquire();
                System.out.println("T2222");
                Thread.sleep(2000);
                System.out.println("T2222");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                //使用完毕，释放锁。+1  0->1
                semaphore.release();
            }
        }).start();
    }
}