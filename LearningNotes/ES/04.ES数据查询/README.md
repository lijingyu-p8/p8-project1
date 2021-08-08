# Elasticsearch

## Query DSL

### 1、指定id查询

```
GET product/_doc/8888
```

### 2、match_all

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

### 3、match

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

### 4、term

- 关键词不会被分词，当作一个完整的短语进行匹配

  ```json
  GET product/_search
  {
    "query": {
      "term": {
        "name": {
          "value": "xiaomi phone"
        }
      }
    }
  }
  ```

  会使用完整的“xiaomi phone”进行数据匹配。所以有可能导致会搜索不到匹配的记录。

- 

### 5、terms

- 和term查询效果相同，但是可以指定多个值。只要其中任何一个满足条件，文档即匹配。

  ```json
  GET product/_search
  {
    "query": {
      "terms": {
        "name": [
          "phone",
          "nfc"
        ]
      }
    }
  }
  ```

- 

### 6、_source

- 指定查询字段。默认情况下，es查询结果会把_source包含的所有字段都返回。可以通过"__source"指定返回的字段。

- includes：来指定想要显示的字段

- excludes：来指定不想要显示的字段

  ```
  GET product/_search
  {
    "query": {
      "match": {
        "name": "xiaomi phone"
      }
    },
    "_source": ["name","price"], 
    "_source": {
      "excludes": ["desc","price"],
      "includes": ["desc","price"]
    }
  }
  ```

- 

### 6、prefix

### 7、sort排序

- 排序，desc、asc

  ```json
  GET product/_search
  {
    "query": {
      "match_all": {}
    },
    "sort": [
      {
        "price": {
          "order": "desc"
        },
        "_score": {
          "order": "asc"
        }
      }
    ]
  }
  ```

## 组合查询

- bool把各种其它查询通过must（必须 ）、 must_not（必须不）、 should（应该）的方式进行组合。

  ```json
  GET product/_search
  {
    "query": {
      "bool": {
        "must": [
          {}
        ],
        "must_not": [
          {}
        ],
        "should": [
          {}
        ],
        "filter": [
          {}
        ],
        "minimum_should_match": 1,
        "boost": 1
      }
    }
  }
  ```

- must

  必须包含，返回的文档必须满足must子句的条件，并且参与计算分值。

- must_not

  必须不包含，返回的文档必须不满足must_not定义的条件。不计算相关度分数。

- should

  可能包含，返回的文档可能满足should子句的条件。在一个Bool查询中，如果没有must或者filter，有一个或者多个should子句，那么只要满足一个就可以返回。

- filter

  过滤，返回的文档必须满足filter子句的条件。但是不会像Must一样，参与计算分值。结果是会被缓存的。

- minimum_should_match

  minimum_should_match参数定义了至少满足几个子句。类似于sql中的in()，计算相关度分数。

- boost

  搜索权重

- filter缓存问题

  ![img](images/filter-1.png)

  1. filter只有当执行到一定次数的时候，才会对热数据进行缓存，缓存的时候会使用二进制数组的形式进行缓存，每条doc会对应0、1，1代表匹配，0代表不匹配。
  2. filer中保存的是匹配结果，所以搜索的时候，可以直接得到结果。
  3. 执行query的时候，filter一般会在query之前进行执行，过滤结果，也可以提高query查询速度。
  4. filter不会计算相关度分数。效率也会比query高。
  5. 当元数据（原始数据）更新的时候，cache也会更新。

## 范围查询

- 查询找出那些落在指定区间内的数字或者时间。 range 查询允许以下字符

  ![](images/range-1.png)

  ```
  GET product/_search
  {
    "query": {
      "range": {
        "price": {
          "gte": 3000,
          "lte": 4000
        }
      }
    }
  }
  ```

## 分页查询

- from：当前页的起始索引，默认从 0 开始。 from = (pageNum - 1) * size

- size：每页显示多少条

  ```
  GET product/_search
  {
    "query": {
      "match_all": {}
    },
    "from": 3,
    "size": 2
  }
  ```

## 模糊查询

- 返回包含与搜索字词相似的字词的文档。编辑距离是将一个术语转换为另一个术语所需的一个字符更改的次数。

- 这些更改可以包括：

  1、更改字符（box → fox）
  2、删除字符（black → lack）
  3、插入字符（sic → sick）
  4、转置两个相邻字符（act → cat）

- 为了找到相似的术语， fuzzy 查询会在指定的编辑距离内创建一组搜索词的所有可能的变体或扩展。然后查询返回每个扩展的完全匹配。

- 通过 fuzziness 修改编辑距离。一般使用默认值 AUTO，根据术语的长度生成编辑距离。

  ```json
  GET product/_search
  {
    "query": {
      "fuzzy": {
        "name": {
          "value": "phronee",
          "fuzziness": 2
        }
      }
    }
  }
  ```

## 聚合查询

- 聚合允许使用者对 es 文档进行统计分析，类似关系型数据库中的 group by，也有很多其他的聚合，例如取最大值、平均值等等。

- 聚合使用aggs关键字，使用"size": 0，可以不返回原始数据

  ```json
  GET product/_search
  {
    "aggs": {
      "MAX_XIAOMI": {
        "max": {//获取最大值
          "field": "price"
        }
      },
      "AGV_XIAOMI":{
        "avg": {//获取平均值
          "field": "price"
        }
      },
      "distinct_age":{
        "cardinality": {//去重之后取总数
          "field": "price"
        }
      },
      "stats":{
        "stats": {//一次返回count， max， min， avg 和 sum 五个指标
          "field": "price"
        }
      },
      "price_groupby":{
        "terms": {//桶聚合，相当于group by分组统计
          "field": "price",
          "size": 10
        },
        "aggs": {//桶聚合嵌套聚合查询
          "price_sum": {
            "sum": {
              "field": "price"
            }
          }
        }
      }
    },
    "size": 0
  }
  ```

  ![image-20210807223439731](images/aggs-1.png)

- 


## 批量查询

- mget实现批量查询，可以一次查询同一索引下的数据，也可以查询不同索引下的数据。get路径中如果包含了index，则body中可以省略。

- 如果查询超时了，会返回超时前查询到的所有数据。

  ```json
  GET /_mget
  GET /<index>/_mget
  body参数说明
  {
      "_id":必填
      "ids":["id1","id2"]也可以使用这种形式查询
      "_index":索引，如果path中指定了，此处可以省略
      "_source"：指定结果中的字段，默认返回所有["field1","field2"]
      "_source": {
          "include": [ "user" ],
          "exclude": [ "user.location" ]
        }
      "_stored_fields":查询store设置为true的字段["field1","field2"]
  }
  ```

  ```json
  GET product2/_mget
  {
    "ids": ["1","2"]
  }
  GET /_mget
  {
    "docs": [
      {
        "_index": "product2",
        "_id": "1",
        "_source":["name"]
        //查询除了name外所有的数据
        "_source":{
          "exclude":["name"]
        }
        //查询store为true的数据
        "stored_fields": [ "field3", "field4" ]
      }
    ]
  }
  ```

- 

## 脚本script

- 查询时使用painless，source指定具体字段，使用doc['filedName'].value

  ```
  GET product/_search
  {
    "script_fields": {
      "price_query": {
        "script": {
          "lang": "painless",
          "source": "doc['price'].value+params.num",
          "params": {
            "num": 2
          }
        }
      }
    }
  }
  ```

  

- 