package com.lijingyu.juc.ato;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class AtomicFieldUpdaterTest {
    public static void main(String[] args) {
        AtomicIntegerFieldUpdater atomicIntegerFieldUpdater = AtomicIntegerFieldUpdater.newUpdater(Student.class, "age");
        Student student = new Student();
        int andIncrement = atomicIntegerFieldUpdater.getAndIncrement(student);
        AtomicReferenceFieldUpdater atomicReferenceFieldUpdater = AtomicReferenceFieldUpdater.newUpdater(Student.class, Address.class, "address");
        Address address = new Address();
        Address address1 = new Address();
        boolean result = atomicReferenceFieldUpdater.compareAndSet(student, address, address1);
    }
}