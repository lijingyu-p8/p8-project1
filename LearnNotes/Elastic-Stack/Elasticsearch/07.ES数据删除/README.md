# **Elasticsearch**

## 数据删除

### 一、单条删除

使用DELETE，对指定id进行删除

```
DELETE product/_doc/8888
```

### 二、条件删除

构建query条件，使用_delete_by_query进行条件删除

```json
POST product/_delete_by_query
{
  "query": {
    "match": {
      "price": 2999
    }
  }
}
```
