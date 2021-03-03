package com.lijingyu.thread03.question1;

import java.util.ArrayList;
import java.util.List;

/**
 * 面试题
 * 实现一个容器，提供两个方法，add size
 * 两个线程，一个往里添加。另一个监控，当达到5个，就报警
 */
public class MyWaitNotify {
    static List<Integer> list = new ArrayList<>();
    static Thread readT = null;
    static Thread addT = null;

    public static void add(int i) {
        list.add(i);
    }

    public static int size() {
        return list.size();
    }

    public static void main(String[] args) {
        Object lock = new Object();

        addT = new Thread(() -> {
            //加锁
            synchronized (lock) {
                for (int i = 0; i < 10; i++) {
                    if (size() == 5) {
                        try {
                            //notify不会释放锁
                            lock.notify();
                            //当前线程阻塞，释放锁
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(i);
                    add(i);
                }
            }
        });

        readT = new Thread(() -> {
            synchronized (lock) {
                System.out.println("读线程启动");
                if (size() != 5) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("检测到5个元素");
                System.out.println("读线程结束");
                lock.notify();
            }
        });
        readT.start();
        addT.start();
    }
}