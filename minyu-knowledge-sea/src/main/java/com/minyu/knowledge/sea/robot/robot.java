package com.minyu.knowledge.sea.robot;

import com.alibaba.fastjson2.JSON;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class robot {
    public static void main(String[] args) throws IOException {
        //该机器人特有的webhookurl
        String url = "";
        //参数
        Map<String, Object> map = new HashMap<>();
        map.put("msgtype", "text");

        Map<String, Object> map2 = new HashMap<>();
        map2.put("content", "猪肉大葱汤饺一份，冰红茶一瓶");
        map.put("text", map2);

        String jsonString = JSON.toJSONString(map);
        HttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);

        StringEntity entity = new StringEntity(jsonString,"UTF-8");

        post.setEntity(entity);
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");

        HttpResponse response = client.execute(post);
        System.out.println(EntityUtils.toString(response.getEntity()));
    }
}
