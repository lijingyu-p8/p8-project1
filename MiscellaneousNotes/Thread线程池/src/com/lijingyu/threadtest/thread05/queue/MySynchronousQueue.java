package com.lijingyu.threadtest.thread05.queue;

import java.util.concurrent.*;

public class MySynchronousQueue {
    public static void main(String[] args) throws InterruptedException {
        SynchronousQueue<String> strs = new SynchronousQueue<>();
        new Thread(()->{
            try {
                System.out.println(strs.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        strs.put("aaa"); //阻塞等待消费者消费
        System.out.println(strs.size());
    }
}