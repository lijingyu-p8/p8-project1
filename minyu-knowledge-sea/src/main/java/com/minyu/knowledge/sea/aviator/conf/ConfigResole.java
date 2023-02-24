package com.minyu.knowledge.sea.aviator.conf;

import java.util.Map;

/**
 * @Description: 配置解析
 * @Author: lijingyu
 * @CreateTime: 2023-02-24  22:42
 */
public class ConfigResole {
    private Map<String, ReCallConfig> reCallConfigMap;

    private MergeSortConfig mergeSortConfig;

    public Map<String, ReCallConfig> getReCallConfigMap() {
        return reCallConfigMap;
    }

    public void setReCallConfigMap(Map<String, ReCallConfig> reCallConfigMap) {
        this.reCallConfigMap = reCallConfigMap;
    }

    public MergeSortConfig getMergeSortConfig() {
        return mergeSortConfig;
    }

    public void setMergeSortConfig(MergeSortConfig mergeSortConfig) {
        this.mergeSortConfig = mergeSortConfig;
    }
}