package com.es.estest.client;

import com.es.estest.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FuzzyQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.MaxAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;

import java.io.IOException;
import java.util.Map;

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
            conditionSearch(restHighLevelClient);
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

    public static void updateData(RestHighLevelClient client) throws IOException {
        // 修改文档 - 请求对象
        UpdateRequest request = new UpdateRequest();
        // 配置修改参数
        request.index("user_index").id("0001");
        // 设置请求体，对数据进行修改
        request.doc(XContentType.JSON, "sex", "女");
        // 客户端发送请求，获取响应对象
        UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
        System.out.println("_index:" + response.getIndex());
        System.out.println("_id:" + response.getId());
        System.out.println("_result:" + response.getResult());
    }

    public static void queryData(RestHighLevelClient client) throws IOException {
        //1.创建请求对象
        GetRequest request = new GetRequest().index("user_index").id("0001");
        //2.客户端发送请求，获取响应对象
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        //3.打印结果信息
        System.out.println("_index:" + response.getIndex());
        System.out.println("_type:" + response.getType());
        System.out.println("_id:" + response.getId());
        System.out.println("source:" + response.getSourceAsString());
    }

    public static void deleteData(RestHighLevelClient client) throws IOException {
        DeleteRequest request = new DeleteRequest("user_index").id("0001");
        DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
        System.out.println(response.toString());
    }

    public static void bulkInsertData(RestHighLevelClient client) throws IOException {
        //创建批量新增请求对象
        BulkRequest request = new BulkRequest();
        request.add(new IndexRequest().index("user_index").id("1001").source(XContentType.JSON, "name", "zhangsan"));
        request.add(new IndexRequest().index("user_index").id("1002").source(XContentType.JSON, "name", "lisi"));
        request.add(new IndexRequest().index("user_index").id("1003").source(XContentType.JSON, "name", "wangwu"));
        //客户端发送请求，获取响应对象
        BulkResponse responses = client.bulk(request, RequestOptions.DEFAULT);
        //打印结果信息
        System.out.println("took:" + responses.getTook());
        System.out.println("items:" + responses.getItems());
    }

    public static void bulkDeleteData(RestHighLevelClient client) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(new DeleteRequest().index("user_index").id("1001"));
        bulkRequest.add(new DeleteRequest().index("user_index").id("1002"));
        bulkRequest.add(new DeleteRequest().index("user_index").id("1003"));
        client.bulk(bulkRequest, RequestOptions.DEFAULT);
    }

    public static void conditionSearch(RestHighLevelClient client) throws IOException {
        // 创建搜索请求对象
        SearchRequest request = new SearchRequest();
        request.indices("user_index");
        // 构建查询的请求体
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 查询所有数据
//        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.query(QueryBuilders.termQuery("name", "lining"));
//        searchSourceBuilder.from(0);
//        searchSourceBuilder.size(10);
//        searchSourceBuilder.sort("age", SortOrder.DESC);
//        String[] includes = new String[1];
//        includes[0] = "sex";
//        String[] excludes = new String[0];
//        searchSourceBuilder.fetchSource(includes, excludes);
        request.source(searchSourceBuilder);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termQuery("name", "lining"));
        boolQueryBuilder.must(QueryBuilders.termQuery("sex", "女"));
        searchSourceBuilder.query(boolQueryBuilder);
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("range");
        rangeQueryBuilder.gte(30);
        rangeQueryBuilder.lte(20);
        FuzzyQueryBuilder fuzzyQueryBuilder = QueryBuilders.fuzzyQuery("name", "zangsan");\
        fuzzyQueryBuilder.fuzziness(Fuzziness.AUTO);
        //构建高亮字段
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font color='red'>");//设置标签前缀
        highlightBuilder.postTags("</font>");//设置标签后缀
        highlightBuilder.field("name");//设置高亮字段
        // 设置高亮构建对象
        searchSourceBuilder.highlighter(highlightBuilder);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        MaxAggregationBuilder maxAge = AggregationBuilders.max("maxAge");
        maxAge.field("age");
        sourceBuilder.aggregation(maxAge);
        TermsAggregationBuilder age_groupby = AggregationBuilders.terms("age_groupby");
        age_groupby.field("age");
        sourceBuilder.aggregation(age_groupby);

        SearchResponse searchResponse = client.search(request, RequestOptions.DEFAULT);
        // 输出查询基本信息
        SearchHits searchHits = searchResponse.getHits();
        System.out.println("took:" + searchResponse.getTook());
        System.out.println("timeout:" + searchResponse.isTimedOut());
        System.out.println("total:" + searchHits.getTotalHits());
        System.out.println("MaxScore:" + searchHits.getMaxScore());
        // 输出每条数据
        SearchHit[] hits = searchResponse.getHits().getHits();
        for (SearchHit hit : hits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            System.out.println(sourceAsMap);
        }
    }
}