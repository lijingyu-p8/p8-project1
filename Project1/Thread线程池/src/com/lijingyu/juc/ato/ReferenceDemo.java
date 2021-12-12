package com.lijingyu.juc.ato;

import java.lang.ref.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ReferenceDemo {
    public static void main(String[] args) {
        ReferenceQueue<MyObject> referenceQueue = new ReferenceQueue();
        PhantomReference<MyObject> phantomReference = new PhantomReference<>(new MyObject(), referenceQueue);
        //System.out.println(phantomReference.get());

        List<byte[]> list = new ArrayList<>();

        new Thread(() -> {
            while (true) {
                list.add(new byte[1 * 1024 * 1024]);
                try {
                    TimeUnit.MILLISECONDS.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(phantomReference.get());
            }
        }, "t1").start();

        new Thread(() -> {
            while (true) {
                Reference<? extends MyObject> reference = referenceQueue.poll();
                if (reference != null) {
                    System.out.println("***********有虚对象加入队列了");
                }
            }
        }, "t2").start();

        //暂停几秒钟线程
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void weakReference() {
        WeakReference<MyObject> weakReference = new WeakReference<>(new MyObject());
        System.out.println("-----gc before内存够用: " + weakReference.get());

        System.gc();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("-----gc after内存够用: " + weakReference.get());
    }

    public static void softReference() {
        //当我们内存不够用的时候，soft会被回收的情况，设置我们的内存大小：-Xms10m -Xmx10m
        SoftReference<MyObject> softReference = new SoftReference<>(new MyObject());

        System.gc();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("-----gc after内存够用: " + softReference.get());

        try {
            byte[] bytes = new byte[9 * 1024 * 1024];
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("-----gc after内存不够: " + softReference.get());
        }
    }

    public static void strongReference() {
        MyObject myObject = new MyObject();
        System.out.println("-----gc before: " + myObject);

        myObject = null;
        System.gc();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("-----gc after: " + myObject);
    }
}