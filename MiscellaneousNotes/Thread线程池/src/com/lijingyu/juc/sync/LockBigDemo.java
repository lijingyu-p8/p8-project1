package com.lijingyu.juc.sync;

public class LockBigDemo {
    static Object objectLock = new Object();
    public static void main(String[] args) {
        new Thread(() -> {
            //会合并为一个锁
            synchronized (objectLock) {
                System.out.println("11111");
            }
            synchronized (objectLock) {
                System.out.println("22222");
            }
            synchronized (objectLock) {
                System.out.println("33333");
            }
        }, "a").start();
    }
}