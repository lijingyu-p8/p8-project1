package com.lijingyu.thread04.reference;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

/**
 * 弱引用
 */
public class MyWeakReference {
    public static void main(String[] args) throws InterruptedException {
        WeakReference<byte[]> weakReference = new WeakReference<>(new byte[1024 * 1024 * 10]);
        System.out.println(weakReference.get());
        //设置堆内存为20M
        System.gc();
        TimeUnit.SECONDS.sleep(1);
        System.out.println(weakReference.get());
        //弱引用只要遇到垃圾回收，就会被回收
        byte[] bytes = new byte[1024 * 1024 * 11];
        System.out.println(weakReference.get());
    }
}