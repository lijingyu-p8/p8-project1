package com.lijingyu.juc.ato;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class ArrayAtomicTest {
    public static void main(String[] args) {
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(5);
        for (int i = 0; i < 5; i++) {
            int andDecrement = atomicIntegerArray.getAndDecrement(i);
            atomicIntegerArray.getAndSet(i, 200);
        }
    }
}