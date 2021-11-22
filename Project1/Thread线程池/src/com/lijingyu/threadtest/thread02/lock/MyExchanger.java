package com.lijingyu.threadtest.thread02.lock;

import java.util.concurrent.Exchanger;

public class MyExchanger {
    static Exchanger<String> exchanger = new Exchanger<>();
    public static void main(String[] args) {
        new Thread(() -> {
            String str = "T1";
            try {
                str = exchanger.exchange(str);//阻塞的
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "--" + str);
        }, "t1").start();

        new Thread(() -> {
            String str = "T2";
            try {
                str = exchanger.exchange(str);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "--" + str);
        }, "t2").start();
    }
}