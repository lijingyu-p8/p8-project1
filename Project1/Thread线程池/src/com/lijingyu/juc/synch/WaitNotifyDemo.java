package com.lijingyu.juc.synch;

import java.util.concurrent.TimeUnit;

public class WaitNotifyDemo {
    public static void main(String[] args) {
        //同一把锁，类似资源类
        Object objectLock = new Object();

        new Thread(() -> {
            synchronized (objectLock) {
                System.out.println(Thread.currentThread().getName() + "\t" + "阻塞了");
                try {
                    objectLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + "\t" + "被唤醒了");
        }, "t1").start();

        //暂停几秒钟线程
        try {
            TimeUnit.SECONDS.sleep(3L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            synchronized (objectLock) {
                objectLock.notify();
            }

        }, "t2").start();
    }
}