package com.lijingyu.second.stringtest;

public class StringTest7 {
    public static void main(String[] args) {
        String s = new String("1");  // 在常量池中已经有了
        s.intern(); // 将该对象放入到常量池。但是调用此方法没有太多的区别，因为已经存在了1
        String s2 = "1";
        System.out.println(s == s2); // false

        String s3 = new String("1") + new String("1");
        s3.intern();
        String s4 = "11";
        System.out.println(s3 == s4); // false
    }

    public void test() {
        String str = new String("ab");
    }

    public void test2() {
        String str = new String("a") + new String("b");
    }
}
