package com.lijingyu.second;

public class LocalVariablesTest {
    public int test() {
        int a = 1;
        int b = 2;
        int c = a + b;
        return c;
    }

    public int test2(String str) {
        int a = 1;
        long d = 111l;
        int b = 2;
        int c = a + b;
        return c;
    }

    public static int test3() {
        int a = 1;
        int b = 2;
        int c = a + b;
        return c;
    }

    public static int test4() {
        int a = 1;
        test3();
        return a;
    }
}













