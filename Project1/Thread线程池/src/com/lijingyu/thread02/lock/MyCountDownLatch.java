package com.lijingyu.thread02.lock;

import java.util.concurrent.CountDownLatch;

public class MyCountDownLatch {
    public static void main(String[] args) {
        Thread[] threads = new Thread[100];
        CountDownLatch countDownLatch = new CountDownLatch(threads.length);
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                System.out.println("---");
                countDownLatch.countDown();
            });
        }
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
        try {
            countDownLatch.await();//相当于一个门闩
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("线程执行完毕");
    }
}
