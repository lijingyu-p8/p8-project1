package com.minyu.knowledge.sea.aviator;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.minyu.knowledge.sea.aviator.sort.AbsoluteSort;

import java.util.ArrayList;
import java.util.List;
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
        JSONObject jsonObject = new JSONObject();
        List<String> categoryList = FunctionTypeRel.categoryList;
        List<Formula> formulas = new ArrayList<>();
        for (int i = 0; i < categoryList.size(); i++) {
            if (jsonObject.containsKey(categoryList.get(i))) {
                JSONArray jsonArray = jsonObject.getJSONArray(categoryList.get(i));
                int size = jsonArray.size();
                for (int j = 0; j < size; j++) {
                    AbsoluteSort absoluteSort = new AbsoluteSort();
                    absoluteSort.init(jsonArray.getJSONObject(j));
                    formulas.add(absoluteSort);
                }
            }
        }
        String category = formulas.get(0).getCategory();
    }
}