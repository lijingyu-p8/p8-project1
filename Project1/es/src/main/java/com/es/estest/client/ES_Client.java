package com.es.estest.client;

import com.es.estest.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;

public class ES_Client {
    public static void main(String[] args) {
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
//        CreateIndexRequest createIndexRequest = new CreateIndexRequest("java-index-1");
//        try {
        //创建索引
//            CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
//            boolean acknowledged = createIndexResponse.isAcknowledged();
//            System.out.println("创建索引响应：" + acknowledged);
        //查询索引
//            GetIndexRequest getIndexRequest = new GetIndexRequest("my_index");
//            GetIndexResponse getIndexResponse = restHighLevelClient.indices().get(getIndexRequest, RequestOptions.DEFAULT);
//            System.out.println(getIndexResponse.getMappings());
//            System.out.println(getIndexResponse.getSettings());
//            //删除索引
//            DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("index");
//            AcknowledgedResponse delete = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
//            System.out.println(delete.isAcknowledged());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try {
            insertData(restHighLevelClient);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            restHighLevelClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void insertData(RestHighLevelClient client) throws IOException {
        // 新增文档 - 请求对象
        IndexRequest request = new IndexRequest();
        // 设置索引及唯一性标识
        request.index("user_index").id("0001");
        // 创建数据对象
        User user = new User();
        user.setAge(23);
        user.setName("lining");
        user.setSex("男");
        user.setId("0001");
        ObjectMapper objectMapper = new ObjectMapper();
        // 添加文档数据，数据格式为 JSON 格式
        String user_json = objectMapper.writeValueAsString(user);
        request.source(user_json, XContentType.JSON);
        // 客户端发送请求，获取响应对象
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        // 打印结果信息
        System.out.println("_index:" + response.getIndex());
        System.out.println("_id:" + response.getId());
        System.out.println("_result:" + response.getResult());
    }
}
