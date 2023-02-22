package com.minyu.knowledge.sea.aviator.es;

import co.elastic.clients.elasticsearch._types.Script;
import com.minyu.knowledge.sea.aviator.RequestContext;

/**
 * @Description: es 召回阶段排序
 * @Author: lijingyu
 * @CreateTime: 2023-02-22  22:47
 */
public interface EsScriptRecallBoost {

    /**
     * 构建混合评分
     *
     * @param requestContext
     * @return
     */
    Script buildScriptMixBoost(RequestContext requestContext);
}