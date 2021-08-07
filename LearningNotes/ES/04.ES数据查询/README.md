# Elasticsearch

## Query DSL

### 1.指定id查询

```
GET product/_doc/8888
```

### 2.match_all

- 查询所有

  ```
  GET student/_search
  {
    "query": {
      "match_all": {}
    }
  }
  # "query"：这里的 query 代表一个查询对象，里面可以有不同的查询属性
  # "match_all"：查询类型，例如： match_all(代表查询所有)， match， term ， range 等等
  # {查询条件}：查询条件会根据类型的不同，写法也有差异
  ```

  ![image-20210807114649608](images/match_all.png)

### 3.match

- 条件匹配，关键词会被分词

- 简单搜索

  ```json
  GET student/_search
  {
    "query": {
      "match": {
        "name": "zhangsan"
      }
    }
  }
  ```

- 指定参数

  ```json
  GET student/_search
  {
    "query": {
      "match": {
        "name": {
          "query": "to be or not to be",
          "operator": "and", 
          "minimum_should_match": 1, 
          "zero_terms_query": "all", 
          "fuzziness": 1,
          "auto_generate_synonyms_phrase_query": "true",
          "analyzer": "standard"
        }
      }
    }
  }
  ```

  1. query: 查询的关键词
  2. operator: match查询。关键词是会被分词，查询结果是bool类型，默认是or，可以指定为and，只有全部分词结果匹配才是目标数据。 
  3. minimum_should_match: 最小匹配数量，默认是1，比如my name li 分词后是三个词，若设置为2，只有其中两个匹配上了，才算匹配成功。
  4. zero_terms_query: 零词查询，ES查询默认是会对无效词进行过滤的，is、a、he、or这类词会被过滤掉。设置为"all"则不会被过滤，比如查询"to be or not to be"，就不能被过滤，否则没有结果。 
  5. fuzziness: 模糊查询最大误差，并非越大越好，最大误差越大，导致召回率高，但是结果不准确。
  6. auto_generate_synonyms_phrase_query: 启用同义词，比如like love 可以理解为同义词，ES查询时，默认时开启的。
  7. analyzer: 指定分词器，默认的是standard。

- 

### 4.term

关键词不会被分词

### 5.terms

### 6.prefix

## 聚合查询