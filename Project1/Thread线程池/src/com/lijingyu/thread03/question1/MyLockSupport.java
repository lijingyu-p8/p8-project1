package com.lijingyu.thread03.question1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

public class MyLockSupport {
    List<Integer> list = new ArrayList<>();
    Thread readT = null;
    Thread addT = null;

    public static void main(String[] args) {
        MyLockSupport myLockSupport = new MyLockSupport();
        myLockSupport.addT = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                if (myLockSupport.size() == 5) {
                    LockSupport.unpark(myLockSupport.readT);
                    LockSupport.park();
                }
                System.out.println(i);
                myLockSupport.add(i);
            }
            LockSupport.unpark(myLockSupport.readT);
        });

        myLockSupport.readT = new Thread(() -> {
            LockSupport.park();
            System.out.println("检测到5个元素");
            LockSupport.unpark(myLockSupport.addT);
        });
        myLockSupport.addT.start();
        myLockSupport.readT.start();
    }

    public void add(int i) {
        list.add(i);
    }

    public int size() {
        return list.size();
    }
}