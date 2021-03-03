package com.lijingyu.thread02.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class MyLockSupport {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println(i);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (i == 5) {
                    LockSupport.park();//使当前线程阻塞
                }
            }
        });
        thread.start();
        try {
            TimeUnit.SECONDS.sleep(8);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("解锁");
        LockSupport.unpark(thread);//解锁thread
    }
}