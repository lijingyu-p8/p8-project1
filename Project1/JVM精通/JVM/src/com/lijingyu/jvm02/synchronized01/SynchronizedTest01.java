package com.lijingyu.jvm02.synchronized01;

public class SynchronizedTest01 {
    synchronized void m(){

    }

    void n(){
        synchronized (this){

        }
    }
}
