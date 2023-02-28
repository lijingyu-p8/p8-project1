package com.minyu.knowledge.sea.aviator.es.function.intf;

import co.elastic.clients.elasticsearch._types.Script;
import com.alibaba.fastjson2.JSONObject;
import com.minyu.knowledge.sea.aviator.RequestContext;

/**
 * @Description: es 召回阶段排序
 * @Author: lijingyu
 * @CreateTime: 2023-02-22  22:47
 */
public interface EsScriptRecallBoost extends FunctionDescribe{

    /**
     * 构建混合评分
     *
     * @param requestContext
     * @param jsonObject
     * @return
     */
    Script buildScriptMixBoost(RequestContext requestContext, JSONObject jsonObject);

    /**
     * 函数名称
     *
     * @return
     */
    String functionName();
}