package com.lijingyu.threadtest.thread03.question3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 顺序打印1A2B
 */
public class MyWaitNotifyQustion {
    Thread t1;
    Thread t2;
//    第一种
//    public static void main(String[] args) {
//        char[] aI = "1234567".toCharArray();
//        char[] aC = "ABCDEFG".toCharArray();
//        MyWaitNotifyQustion myWaitNotifyQustion = new MyWaitNotifyQustion();
//        myWaitNotifyQustion.t1 = new Thread(()->{
//            for (int i = 0; i < aI.length; i++) {
//                System.out.print(aI[i]);
//                LockSupport.unpark(myWaitNotifyQustion.t2);
//                LockSupport.park();
//            }
//        });
//        myWaitNotifyQustion.t2 = new Thread(()->{
//            for (int i = 0; i < aC.length; i++) {
//                LockSupport.park();
//                System.out.print(aC[i]);
//                LockSupport.unpark(myWaitNotifyQustion.t1);
//            }
//        });
//        myWaitNotifyQustion.t1.start();;
//        myWaitNotifyQustion.t2.start();
//    }

//    public static void main(String[] args) {
//        Object o = new Object();
//        char[] aI = "1234567".toCharArray();
//        char[] aC = "ABCDEFG".toCharArray();
//        new Thread(()->{
//            synchronized (o){
//                for (int i = 0; i < aI.length; i++) {
//                    System.out.print(aI[i]);
//                    try {
//                        o.notify();
//                        o.wait();//让出锁
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                o.notify();
//            }
//        }).start();
//
//        new Thread(()->{
//            synchronized (o){
//                for (int i = 0; i < aC.length; i++) {
//                    try {
//                        System.out.print(aC[i]);
//                        o.notify();
//                        o.wait();//让出锁
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                o.notify();
//            }
//        }).start();
//    }

    public static void main(String[] args) {
        char[] aI = "1234567".toCharArray();
        char[] aC = "ABCDEFG".toCharArray();

        Lock lock = new ReentrantLock();
        Condition conditionT1 = lock.newCondition();
        Condition conditionT2 = lock.newCondition();

        new Thread(() -> {
            try {
                lock.lock();
                for (char c : aI) {
                    System.out.print(c);
                    conditionT2.signal();
                    conditionT1.await();
                }
                conditionT2.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "t1").start();

        new Thread(() -> {
            try {
                lock.lock();
                for (char c : aC) {
                    System.out.print(c);
                    conditionT1.signal();
                    conditionT2.await();
                }
                conditionT1.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "t2").start();
    }
}