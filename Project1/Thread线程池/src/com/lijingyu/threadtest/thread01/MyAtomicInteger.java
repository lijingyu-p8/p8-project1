package com.lijingyu.threadtest.thread01;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MyAtomicInteger {
    AtomicInteger count = new AtomicInteger(0);

    public static void main(String[] args) {
        MyAtomicInteger myAtomicInteger = new MyAtomicInteger();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            threads.add(new Thread(myAtomicInteger::m));
        }
        threads.forEach(thread -> {
            thread.start();
        });
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(myAtomicInteger.count);
    }

    public void m(){
        for (int i = 0; i < 10_000; i++) {
            count.incrementAndGet();
        }
    }
}
