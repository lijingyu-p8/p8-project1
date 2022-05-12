package com.lijingyu.juc.sync;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockDownGradingDemo {
    public static void main(String[] args) {
        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
        writeLock.lock();
        System.out.println("-------正在写入");
        readLock.lock();
        System.out.println("-------正在读取");
        writeLock.unlock();
    }
}