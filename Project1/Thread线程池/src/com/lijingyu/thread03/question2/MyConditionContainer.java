package com.lijingyu.thread03.question2;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 面试题：写一个固定容量同步容器，支持put get方法。
 * 能够支持10个消费者，2个生产者阻塞调用
 * @param <T>
 */
public class MyConditionContainer<T> {
    final private LinkedList<T> lists = new LinkedList<>();
    final private int MAX = 10; //最多10个元素
    private int count = 0;
    private Lock lock = new ReentrantLock();
    private Condition producer = lock.newCondition();
    private Condition consumer = lock.newCondition();

    public static void main(String[] args) throws Exception {
        MyConditionContainer<String> c = new MyConditionContainer<>();
        //启动消费者线程
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 5; j++) {
                    System.out.println(c.get());
                }
            }, "consumer" + i).start();
        }
        TimeUnit.SECONDS.sleep(2);
        //启动生产者线程
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                for (int j = 0; j < 25; j++) {
                    c.put(Thread.currentThread().getName() + "_" + j);
                }
            }, "producer" + i).start();
        }
    }

    public void put(T t) {
        try {
            lock.lock();
            //满了就阻塞住。要一直用while判断，因为获取到锁之后，如果不再判断，其他线程有可能
            //在当前线程wait的时候，已经装满了
            while (lists.size() == MAX) {
                try {
                    producer.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            lists.add(t);
            ++count;
            consumer.signalAll(); //通知消费者线程进行消费
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public T get() {
        T t = null;
        try {
            lock.lock();
            while (lists.size() == 0) {
                try {
                    consumer.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            t = lists.removeFirst();
            count--;
            producer.signalAll(); //通知生产者进行生产
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return t;
    }
}