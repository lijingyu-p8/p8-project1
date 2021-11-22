package com.lijingyu.threadtest.thread04.reference;

import java.lang.ref.SoftReference;
import java.util.concurrent.TimeUnit;

/**
 * 软引用
 */
public class MySoftReference {
    public static void main(String[] args) throws InterruptedException {
        SoftReference<byte[]> softReference = new SoftReference<>(new byte[1024 * 1024 * 10]);
        System.out.println(softReference.get());
        //设置堆内存为20M
        System.gc();
        TimeUnit.SECONDS.sleep(1);
        System.out.println(softReference.get());
        //内存不够用的时候，软引用才会被回收，否则不会被回收
        byte[] bytes = new byte[1024 * 1024 * 11];
        System.out.println(softReference.get());
    }
}