package com.lijingyu.thread02.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyReentrantLock {
    /**
     * 默认法false，非公平锁
     */
    Lock lock = new ReentrantLock(true);

    public void m1() {
        try {
            lock.lock();//获得锁
            for (int i = 0; i < 10; i++) {
                TimeUnit.SECONDS.sleep(1);
                System.out.println(i);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();//释放锁
        }
    }

    public void m2() {
        boolean lock = false;
        try {
            lock = this.lock.tryLock(13, TimeUnit.SECONDS);//尝试获取锁3秒
            System.out.println("t2......" + lock);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (lock) {
                this.lock.unlock();//释放锁
            }
        }
    }

    public static void main(String[] args) {
        MyReentrantLock t = new MyReentrantLock();
        new Thread(t::m1).start();
        new Thread(t::m2).start();
    }
}