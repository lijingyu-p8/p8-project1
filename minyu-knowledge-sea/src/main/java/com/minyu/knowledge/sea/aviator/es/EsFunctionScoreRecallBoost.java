package com.minyu.knowledge.sea.aviator.es;

import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScore;
import com.minyu.knowledge.sea.aviator.RequestContext;

/**
 * @Description: es 召回阶段排序
 * @Author: lijingyu
 * @CreateTime: 2023-02-22  22:47
 */
public interface EsFunctionScoreRecallBoost {

    /**
     * 构建function score
     *
     * @param requestContext
     * @return
     */
    FunctionScore buildFunctionScoreBoost(RequestContext requestContext);

    /**
     * 分数还原
     *
     * @param originScore
     * @return
     */
    double reScore(double originScore);

    String functionName();
}