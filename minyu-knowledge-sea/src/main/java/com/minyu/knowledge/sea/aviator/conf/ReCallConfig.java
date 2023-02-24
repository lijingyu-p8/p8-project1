package com.minyu.knowledge.sea.aviator.conf;

import com.alibaba.fastjson2.JSONObject;

/**
 * @Description: 召回配置
 * @Author: lijingyu
 * @CreateTime: 2023-02-24  22:36
 */
public class ReCallConfig {
    private String serviceName;

    private JSONObject sortJson;

    private int size;

    private JSONObject originJson;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public JSONObject getSortJson() {
        return sortJson;
    }

    public void setSortJson(JSONObject sortJson) {
        this.sortJson = sortJson;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public JSONObject getOriginJson() {
        return originJson;
    }

    public void setOriginJson(JSONObject originJson) {
        this.originJson = originJson;
    }
}