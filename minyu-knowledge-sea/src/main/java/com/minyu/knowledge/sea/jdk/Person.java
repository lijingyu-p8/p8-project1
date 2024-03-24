package com.minyu.knowledge.sea.jdk;

public class Person<T> {
    public void eat(T t) {
        System.out.printf("eat {}", t);
    }

}
