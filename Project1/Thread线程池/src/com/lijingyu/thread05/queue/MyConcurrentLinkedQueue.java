package com.lijingyu.thread05.queue;

import java.util.concurrent.ConcurrentLinkedQueue;

public class MyConcurrentLinkedQueue {
    public static void main(String[] args) {
        ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < 10; i++) {
            queue.offer("字符_" + i);
        }
        for (int i = 0; i < 100; i++) {
            new Thread(()->{
                while (!queue.isEmpty()){
                    System.out.println(queue.poll());
                }
            }).start();
        }
    }
}