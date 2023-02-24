package com.minyu.knowledge.sea.aviator.es;

import co.elastic.clients.elasticsearch._types.Script;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.minyu.knowledge.sea.aviator.RequestContext;
import com.minyu.knowledge.sea.aviator.conf.ReCallConfig;
import com.minyu.knowledge.sea.aviator.es.function.intf.EsFunctionScoreRecallBoost;
import com.minyu.knowledge.sea.aviator.es.function.intf.EsScriptRecallBoost;
import com.minyu.knowledge.sea.aviator.handle.HandleConfig;

import java.util.Map;

/**
 * @Description: 构建器
 * @Author: lijingyu
 * @CreateTime: 2023-02-23  22:58
 */
public class EsBuilderScoreManager {

    private Map<String, EsFunctionScoreRecallBoost> esFunctionScoreRecallBoostMap;

    private Map<String, EsScriptRecallBoost> esScriptRecallBoostMap;

    public Script buildEsDslScript(RequestContext requestContext, String serviceName) {
        ReCallConfig recallConfig = HandleConfig.getRecallConfig(serviceName);
        JSONObject sortJson = recallConfig.getSortJson();
        String type = recallConfig.getOriginJson().getString("type");
        JSONArray jsonArray = sortJson.getJSONArray("sort");
        int size = jsonArray.size();
        for (int i = 0; i < size; i++) {
            JSONObject functionJsonObject = jsonArray.getJSONObject(i);
            String functionName = functionJsonObject.getString("functionName");
            if (type.equals("functionScore")) {
                esFunctionScoreRecallBoostMap.get(functionName).buildFunctionScoreBoost(requestContext, functionJsonObject);
            } else {
                esScriptRecallBoostMap.get(functionName).buildScriptMixBoost(requestContext, functionJsonObject);
            }
        }
        return null;
    }

    public void reScore(RequestContext requestContext, String serviceName) {
        ReCallConfig recallConfig = HandleConfig.getRecallConfig(serviceName);
        JSONObject sortJson = recallConfig.getSortJson();
        String type = recallConfig.getOriginJson().getString("type");
        double originScore = 100;
        if (type.equals("functionScore")) {
            JSONArray jsonArray = sortJson.getJSONArray("sort");
            int size = jsonArray.size();
            for (int i = 0; i < size; i++) {
                JSONObject functionJsonObject = jsonArray.getJSONObject(i);
                String functionName = functionJsonObject.getString("functionName");
                esFunctionScoreRecallBoostMap.get(functionName).reScore(originScore, functionJsonObject);
            }
        }
    }
}