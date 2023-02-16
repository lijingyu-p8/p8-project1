package com.minyu.knowledge.sea.aviator.sort;

import com.alibaba.fastjson2.JSONObject;
import com.minyu.knowledge.sea.aviator.Formula;

/**
 * @Description: 相关度排序
 * @Author: lijingyu
 * @CreateTime: 2023-02-16  22:16
 */
public class RelevantSort implements Formula {

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
