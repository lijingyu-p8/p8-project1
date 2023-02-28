package com.minyu.knowledge.sea.aviator.es.function.intf;

import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScore;
import com.alibaba.fastjson2.JSONObject;
import com.minyu.knowledge.sea.aviator.RequestContext;

/**
 * @Description: es 召回阶段排序
 * @Author: lijingyu
 * @CreateTime: 2023-02-22  22:47
 */
public interface EsFunctionScoreRecallBoost extends FunctionDescribe{

    /**
     * 构建function score
     *
     * @param requestContext
     * @param jsonObject
     * @return
     */
    FunctionScore buildFunctionScoreBoost(RequestContext requestContext, JSONObject jsonObject);

    /**
     * 分数还原
     *
     * @param originScore
     * @param jsonObject
     * @return
     */
    double reScore(double originScore, JSONObject jsonObject);

    /**
     * 函数名称
     *
     * @return
     */
    String functionName();
}