package com.minyu.knowledge.sea.jdk;

/**
 * 8--》17的新特性
 */
public class Java17New {
    public static void main(String[] args) {
        // 1、泛型
        fanxing();
    }

    private static void fanxing() {
        //可以不写明泛型类型
        Comparable<String> comparable = new Comparable<>() {
            @Override
            public int compareTo(String o) {
                return 0;
            }
        };
        //可以通过匿名内部类写法,在某个实例上完成对某个方法的重写
        Person<String> person = new Person<>() {
            @Override
            public void eat(String s) {
                super.eat(s);
            }
        };
    }
}
