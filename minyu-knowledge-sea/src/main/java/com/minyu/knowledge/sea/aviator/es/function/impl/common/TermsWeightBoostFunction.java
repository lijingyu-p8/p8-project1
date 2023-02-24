package com.minyu.knowledge.sea.aviator.es.function.impl.functionscore.common;

import co.elastic.clients.elasticsearch._types.InlineScript;
import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScore;
import com.alibaba.fastjson2.JSONObject;
import com.minyu.knowledge.sea.aviator.RequestContext;
import com.minyu.knowledge.sea.aviator.es.function.intf.EsFunctionScoreRecallBoost;
import com.minyu.knowledge.sea.aviator.es.function.intf.EsScriptRecallBoost;

/**
 * @Description: TODO
 * @Author: lijingyu
 * @CreateTime: 2023-02-23  22:42
 */
public class TermsWeightBoostFunction implements EsFunctionScoreRecallBoost, EsScriptRecallBoost {

    @Override
    public FunctionScore buildFunctionScoreBoost(RequestContext requestContext, JSONObject jsonObject) {
        return null;
    }

    @Override
    public double reScore(double originScore, JSONObject jsonObject) {
        return 0;
    }

    @Override
    public Script buildScriptMixBoost(RequestContext requestContext, JSONObject jsonObject) {
        String field = jsonObject.getString("field");
        String fieldValues = jsonObject.getString("fieldValues");
        Double positive = jsonObject.getDouble("positive");
        Double negative = jsonObject.getDouble("negative");
        Script script = Script.of(fn -> fn.inline(InlineScript.of(in -> in.lang("painless")
                .source("xxxx" + fieldValues))));
        return script;
    }

    @Override
    public String functionName() {
        return "termsWeight";
    }
}
