package com.lijingyu.jvm03.runtimedataarea;

/**
 * 运行时数据区
 */
public class RunTimeDataArea01 {
    public static void main(String[] args) {
        int i = 8;
        i = i++;
        System.out.println(i);
        int j = 8;
        j = ++j;
        System.out.println(j);
    }
}
