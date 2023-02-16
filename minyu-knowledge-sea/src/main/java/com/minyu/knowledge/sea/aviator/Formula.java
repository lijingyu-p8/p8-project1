package com.minyu.knowledge.sea.aviator;

import com.alibaba.fastjson2.JSONObject;

/**
 * @Description: TODO
 * @Author: lijingyu
 * @CreateTime: 2023-02-16  22:41
 */
public interface Formula {

    void init(JSONObject jsonObject);

    int getSortLevel();

    String getFormula();

    String getCategory();

    String getStage();

    String getExtFieldMap();
}