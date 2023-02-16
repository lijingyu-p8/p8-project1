package com.minyu.knowledge.sea.aviator.sort;

import com.alibaba.fastjson2.JSONObject;
import com.minyu.knowledge.sea.aviator.Formula;

/**
 * @Description: 绝对排序
 * @Author: lijingyu
 * @CreateTime: 2023-02-16  22:15
 */
public class AbsoluteSort implements Formula {

    private String stageName;

    private int sortLevel;

    private Sort sort;

    private String formula;

    @Override
    public void init(JSONObject jsonObject) {

    }

    @Override
    public int getSortLevel() {
        return 0;
    }

    @Override
    public String getFormula() {
        return null;
    }

    @Override
    public String getCategory() {
        return null;
    }

    @Override
    public String getStage() {
        return null;
    }

    @Override
    public String getExtFieldMap() {
        return null;
    }
}