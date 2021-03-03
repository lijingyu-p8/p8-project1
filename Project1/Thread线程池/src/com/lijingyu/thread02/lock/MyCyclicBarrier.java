package com.lijingyu.thread02.lock;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

public class MyCyclicBarrier {
    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5, () -> {
            System.out.println("发车、放行");
        });
        Thread[] threads = new Thread[20];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                try {
                    System.out.println("来了");
                    TimeUnit.SECONDS.sleep(1);
                    cyclicBarrier.await();//等待
                    System.out.println("栅栏开了");
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }, "线程" + i);
        }
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
    }
}