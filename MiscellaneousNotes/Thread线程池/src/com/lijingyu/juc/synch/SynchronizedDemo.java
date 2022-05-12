package com.lijingyu.juc.synch;

public class SynchronizedDemo {
    Object object = new Object();

    public static synchronized void m3(){
        System.out.println("3333");
    }

    public synchronized void m2(){
        System.out.println("22222");
    }

    public void m1(){
        synchronized (object){
            System.out.println("1111111111");
            throw new RuntimeException("");
        }
    }
}