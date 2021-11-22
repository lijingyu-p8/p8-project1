package com.lijingyu.threadtest.thread03.question1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class MyCountDownLatch {
    List<Integer> list = new ArrayList<>();

    public static void main(String[] args) {
        MyCountDownLatch t = new MyCountDownLatch();

        //设置数量为1
        CountDownLatch readlatch = new CountDownLatch(1);
        CountDownLatch writelatch = new CountDownLatch(1);
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                if (t.size() == 5) {
                    //打开门闩
                    readlatch.countDown();
                    try {
                        writelatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(i);
                t.add(i);
            }
        }).start();

        new Thread(() -> {
            System.out.println("读线程启动");
            if (t.size() != 5) {
                try {
                    readlatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("检测到5个元素");
            System.out.println("读线程结束");
            writelatch.countDown();
        }).start();
    }

    public void add(int i) {
        list.add(i);
    }

    public int size() {
        return list.size();
    }
}