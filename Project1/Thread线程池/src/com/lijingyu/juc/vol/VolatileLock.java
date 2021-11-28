package com.lijingyu.juc.vol;

/**
 * 使用：当读远多于写，结合使用内部锁和 volatile 变量来减少同步的开销
 * 理由：利用volatile保证读取操作的可见性；利用synchronized保证复合操作的原子性
 */
public class VolatileLock {
    private volatile int value;

    public int getValue() {
        return value;   //利用volatile保证读取操作的可见性
    }

    public synchronized int increment() {
        return value++; //利用synchronized保证复合操作的原子性
    }
}