# **Elasticsearch**

## 一、Client

```java
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

public class ESTest_Client {
    public static void main(String[] args) throws Exception {

        // 创建ES客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );
        // 关闭ES客户端
        esClient.close();
    }
}
```

## 二、索引相关操作

### 1、创建索引

```java
CreateIndexRequest createIndexRequest = new CreateIndexRequest("java-index-1");
//create
CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
boolean acknowledged = createIndexResponse.isAcknowledged();
```

### 2、查询索引

```java
GetIndexRequest getIndexRequest = new GetIndexRequest("my_index");
GetIndexResponse getIndexResponse = restHighLevelClient.indices().get(getIndexRequest, RequestOptions.DEFAULT);
System.out.println(getIndexResponse.getMappings());
System.out.println(getIndexResponse.getSettings());
```

### 3、修改索引

### 4、删除索引

```java
//删除索引
DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("index");
AcknowledgedResponse delete = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
```

## 三、数据相关操作

### 1、数据新增

```java
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
```

