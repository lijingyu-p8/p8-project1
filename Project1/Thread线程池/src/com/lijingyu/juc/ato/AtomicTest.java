package com.lijingyu.juc.ato;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicTest {
    public static void main(String[] args) {
        AtomicReference<Student> atomicReference = new AtomicReference<>();
        Student student1 = new Student();
        Student student2 = new Student();
        atomicReference.getAndSet(student1);
        atomicReference.compareAndSet(student1, student2);
    }
}