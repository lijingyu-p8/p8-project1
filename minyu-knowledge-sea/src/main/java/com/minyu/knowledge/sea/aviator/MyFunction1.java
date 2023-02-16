package com.minyu.knowledge.sea.aviator;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.Map;

/**
 * @Description:
 * @Author: lijingyu
 * @CreateTime: 2023-02-16  21:43
 */
public class MyFunction1 extends AbstractFunction {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        return super.call(env, arg1, arg2);
    }

    public static void main(String[] args) {
        AviatorEvaluator.addFunction(new MyFunction1());
    }
}