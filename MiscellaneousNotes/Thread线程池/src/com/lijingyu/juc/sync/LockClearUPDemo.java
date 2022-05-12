package com.lijingyu.juc.sync;

public class LockClearUPDemo {
    static Object objectLock = new Object();//正常的

    public static void main(String[] args) {
        LockClearUPDemo demo = new LockClearUPDemo();

        for (int i = 1; i <= 10; i++) {
            new Thread(() -> demo.m1(), String.valueOf(i)).start();
        }
    }

    public void m1() {
        //锁消除,JIT会无视它，synchronized(对象锁)不存在了。不正常的
        Object o = new Object();
        synchronized (o) {
            System.out.println("-----hello LockClearUPDemo" + "\t" + o.hashCode() + "\t" + objectLock.hashCode());
        }
    }
}