package com.lijingyu.juc.ato;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class ABADemo2 {
    static AtomicMarkableReference<Integer> markableReference = new AtomicMarkableReference<>(100, false);

    public static void main(String[] args) {
        new Thread(() -> {
            boolean marked = markableReference.isMarked();
            System.out.println(Thread.currentThread().getName() + "\t 1次版本号" + marked);
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            markableReference.compareAndSet(100, 101, marked, !marked);
            System.out.println(Thread.currentThread().getName() + "\t 2次版本号" + markableReference.isMarked());
            markableReference.compareAndSet(101, 100, markableReference.isMarked(), !markableReference.isMarked());
            System.out.println(Thread.currentThread().getName() + "\t 3次版本号" + markableReference.isMarked());
        }, "t5").start();

        new Thread(() -> {
            boolean marked = markableReference.isMarked();
            System.out.println(Thread.currentThread().getName() + "\t 1次版本号" + marked);
            //暂停几秒钟线程
            try {
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            markableReference.compareAndSet(100, 2020, marked, !marked);
            System.out.println(Thread.currentThread().getName() + "\t" + markableReference.getReference() + "\t" + markableReference.isMarked());
        }, "t6").start();
    }
}