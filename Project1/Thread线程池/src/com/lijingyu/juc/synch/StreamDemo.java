package com.lijingyu.juc.synch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class StreamDemo {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            list.add("qq" + i);
        }
        ConcurrentSkipListSet<String> list2 = new ConcurrentSkipListSet<>();
        new CopyOnWriteArrayList<String>();
        list.parallelStream().forEach(item ->{
            list2.add(item);
//            try {
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println(Thread.currentThread().getName() + item);
        });
        System.out.println(list2.size());
    }
}