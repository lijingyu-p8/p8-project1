package com.lijingyu.juc.synch;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class LockSupportDemo {
    public static void main(String[] args) {
        //正常使用+不需要锁块
        Thread t1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " " + "1111111111111");
            LockSupport.park();
            System.out.println(Thread.currentThread().getName() + " " + "2222222222222------end被唤醒");
        }, "t1");
        t1.start();
        //暂停几秒钟线程
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LockSupport.unpark(t1);
        System.out.println(Thread.currentThread().getName() + "   -----LockSupport.unparrk() invoked over");
    }
}