package com.minyu.knowledge.sea.aviator.recall;

import com.alibaba.fastjson2.JSONObject;

/**
 * @Description: TODO
 * @Author: lijingyu
 * @CreateTime: 2023-02-23  22:36
 */
public class EsRecall implements ReCallService {

    private int size;

    @Override
    public void build(JSONObject jsonObject) {

    }

    @Override
    public int getCallSize() {
        return size;
    }

    @Override
    public String getServiceName() {
        return "service_name_es";
    }

    @Override
    public Class getServiceClassType() {
        return EsRecall.class;
    }
}
