# **Elasticsearch**

## 数据修改

### 一、覆盖更新

- 单条覆盖更新

  使用与新增相同的url请求，当id不变时，每次新的请求体数据会覆盖旧的数据，并更新version。数据实际上没有被修改，只是按照唯一id，对数据生成了最新的版本。

  ```
  POST product/_doc/8888
  {
    "name": "AAA xiaomi phone",
    "desc": "shouji zhong de zhandouji",
    "price": 1199,
    "tags": [
      "xingjiabi",
      "fashao",
      "buka"
    ]
  }
  ```

- 批量覆盖更新



### 二、增量修改字段

- 单条数据修改

  使用_update关键字，对指定id下具体数据进行更新。“doc”实际为type，因为7.0之后弱化了type，默认为doc，所以更新时使用默认的“doc”。

  ```
  POST product/_update/8888
  {
    "doc": {
      "price": 2999//将price更新为2999
    }
  }
  ```

  

- 批量数据修改
