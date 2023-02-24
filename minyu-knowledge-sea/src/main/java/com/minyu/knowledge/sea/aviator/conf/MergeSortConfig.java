package com.minyu.knowledge.sea.aviator.conf;

import com.alibaba.fastjson2.JSONObject;

import java.util.List;

/**
 * @Description: 合并排序配置
 * @Author: lijingyu
 * @CreateTime: 2023-02-24  22:39
 */
public class MergeSortConfig {
    private List<String> needFieldList;

    private String sortFormula;

    private int size;

    private JSONObject originJson;

    public List<String> getNeedFieldList() {
        return needFieldList;
    }

    public void setNeedFieldList(List<String> needFieldList) {
        this.needFieldList = needFieldList;
    }

    public String getSortFormula() {
        return sortFormula;
    }

    public void setSortFormula(String sortFormula) {
        this.sortFormula = sortFormula;
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