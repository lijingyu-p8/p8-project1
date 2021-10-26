package com.lijingyu.second;

import java.io.Serializable;

/**
 * 测试方法区
 */
public class MethodInnerStrucTest extends Object implements Comparable<String>, Serializable {
    private static String str = "测试方法的内部结构";
    //属性
    public int num = 10;

    public static int test2(int cal) {
        int result = 0;
        try {
            int value = 30;
            result = value / cal;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //构造器
    //方法
    public void test1() {
        int count = 20;
        System.out.println("count = " + count);
    }

    @Override
    public int compareTo(String o) {
        return 0;
    }
}