package com.minyu.knowledge.sea.aviator.es.param;

/**
 * @Description: 特殊参数
 * @Author: lijingyu
 * @CreateTime: 2023-02-23  22:54
 */
public class SpecialParam {
    public static boolean isSpecialParam(String param) {
        if ("expWorkFunc".equals(param)) {
            return true;
        }
        return false;
    }
}