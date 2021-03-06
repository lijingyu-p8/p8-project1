package com.lijingyu.thread05.queue;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class MyLinkedTransferQueue {
    public static void main(String[] args) throws InterruptedException {
        LinkedTransferQueue<String> strs = new LinkedTransferQueue<String>();
        new Thread(()->{
            try {
                System.out.println(strs.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        strs.tryTransfer("aaa", 1,TimeUnit.SECONDS);
//        strs.transfer("aaa"); //阻塞等待消费者消费
        System.out.println(strs.size());
    }
}