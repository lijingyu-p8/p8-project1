package com.minyu.knowledge.sea.aviator.recall;

import com.alibaba.fastjson2.JSONObject;

/**
 * @Description: 召回服务
 * @Author: lijingyu
 * @CreateTime: 2023-02-23  22:32
 */
public interface ReCallService {

    void build(JSONObject jsonObject);

    /**
     * 召回数量
     *
     * @return 数量
     */
    int getCallSize();

    /**
     * 服务名称
     *
     * @return
     */
    String getServiceName();

    /**
     * 召回类
     *
     * @return
     */
    Class getServiceClassType();
}