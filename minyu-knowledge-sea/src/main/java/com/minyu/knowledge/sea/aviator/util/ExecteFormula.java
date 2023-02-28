package com.minyu.knowledge.sea.aviator.util;

import com.googlecode.aviator.AviatorEvaluator;

/**
 * @Description: TODO
 * @Author: lijingyu
 * @CreateTime: 2023-02-28  23:26
 */
public class ExecteFormula {

    public static String getValue(String formula) {
        Object execute = AviatorEvaluator.execute(formula);
        return execute.toString();
    }
}