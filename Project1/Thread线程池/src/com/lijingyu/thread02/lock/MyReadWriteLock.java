package com.lijingyu.thread02.lock;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MyReadWriteLock {
    //读写锁
    static ReadWriteLock lock = new ReentrantReadWriteLock();
    //设置读锁
    static Lock readLock = lock.readLock();
    //设置写锁
    static Lock writeLock = lock.writeLock();
    private static int value;

    //读方法
    public static void read(Lock lock) {
        try {
            lock.lock();
            TimeUnit.SECONDS.sleep(1);
            System.out.println("read~~~~~");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void write(Lock lock, int v) {
        try {
            lock.lock();
            TimeUnit.SECONDS.sleep(1);
            System.out.println("write~~~~");
            value = v;
            System.out.println(value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        Runnable read = () -> {
            read(readLock);
        };
        Runnable write = () -> {
            write(writeLock, new Random().nextInt());
        };
        for (int i = 0; i < 18; i++) {
            new Thread(read).start();
        }
        for (int i = 0; i < 2; i++) {
            new Thread(write).start();
        }
    }
}