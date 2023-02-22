package com.minyu.knowledge.sea.aviator;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.minyu.knowledge.sea.aviator.formula.TermsWeightFormula;

import java.util.List;

/**
 * @Description: TODO
 * @Author: lijingyu
 * @CreateTime: 2023-02-22  22:50
 */
public class RequestContext {
    public static void main(String[] args) {
        AviatorEvaluator.addFunction(new TermsWeightFormula());
        Expression compile = AviatorEvaluator.compile("termsWeight(ddd) + a+b/3.0");
        List<String> variableNames = compile.getVariableNames();
        System.out.println(variableNames);
    }
}