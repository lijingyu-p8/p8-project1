# Elasticsearch查询

## 一、Query DSL

### 1、指定id查询

```
GET product/_doc/8888
```

### 2、match_all

查询所有

```json
GET student/_search
{
  "query": {
    "match_all": {}
  }
}
# "query"：这里的 query 代表一个查询对象，里面可以有不同的查询属性
# "match_all"：查询类型，例如： match_all(代表查询所有)， match， term ， range 等等
# {查询条件}：括号内查询条件会根据类型的不同，写法也有差异
```

![](images/match_all.png)

### 3、match

条件匹配，match 匹配类型查询，会把查询条件进行分词，然后进行查询，多个词条之间是 or 的关系

- 简单搜索

  ```json
  GET student/_search
  {
    "query": {
      "match": {
        "name": "hello world" #默认分词器会将关键词分词成hello、world两个词，同时对两个词进行查询
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
          "analyzer": "standard",
          "boost": 1
        }
      }
    }
  }
  ```

  1. query：查询的关键词
  2. operator：match查询。关键词是会被分词，查询结果是bool类型，默认是or，可以指定为and，只有全部分词结果匹配才是目标数据。 
  3. minimum_should_match：最小匹配数量，默认是1，比如my name li 分词后是三个词，若设置为2，只有其中两个匹配上了，才算匹配成功。
  4. zero_terms_query：零词查询，ES查询默认是会对无效词进行过滤的，is、a、he、or这类词会被过滤掉。设置为"all"则不会被过滤，比如查询"to be or not to be"，就不能被过滤，否则没有结果。 
  5. fuzziness：模糊查询最大误差，并非越大越好，最大误差越大，导致召回率高，但是结果不准确。
  6. auto_generate_synonyms_phrase_query：启用同义词，比如like love 可以理解为同义词，ES查询时，默认时开启的。
  7. analyzer：指定分词器，默认的是standard。
  8. boost：权重，一般在多查询中使用，配置不同字段在计算评分中不同的权重比例。

### 4、match_phrase

短语搜索，和全文检索相反，搜索词会作为一个短语去检索。

```json
GET /product/_search
{
  "query": {
    "match_phrase": {
      "name": "nfc phone"
    }
  }
}
```

### 5、term

关键词不会被分词，当作一个完整的短语进行匹配

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

### 6、terms

terms 查询和 term 查询一样，但它允许你指定多值进行匹配。如果这个字段包含了指定值中的任何一个值，那么这个文档满足条件，类似于 mysql 的 in。

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

name包含“phone”或者“nfc”的，可以进行匹配。

### 7、_source

- 指定查询结果中返回的字段。默认情况下，es查询结果会把_source包含的所有字段都返回。可以通过"__source"指定返回的字段。但是有一点需要注意，es还是会把所有数据都查询，只是再最后返回给client的时候，依据source自定义的字段进行返回。

- 减少不必要的数据返回，可以降低网络压力。

- includes：来指定想要显示的字段

- excludes：来指定不想要显示的字段

  ```json
  GET product/_search
  {
    "query": {
      "match": {
        "name": "xiaomi phone"
      }
    },
    "_source": false,  #不返回source
    "_source": ["name","price"], #指定返回字段
    "_source": {
      "excludes": ["desc","price"], #排除
      "includes": ["desc","price"]  #包括
    }
  }
  ```

### 8、prefix

前缀查询，返回在提供的字段中包含特定前缀的文档。

```json
GET product/_search
{
  "query": {
    "prefix": {
      "name.keyword": {
        "value": "A"
      }
    }
  }
}
```

### 9、sort排序

sort 可以让我们按照不同的字段进行排序，并且通过 order 指定排序的方式。desc 降序，asc升序。

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
#也支持script
GET product/_search
{
  "query": {
    "term": { "user": "kimchy" }
  },
  "sort": {
    "_script": {
      "type": "number",
      "script": {
        "lang": "painless",
        "source": "doc['field_name'].value * params.factor",
        "params": {
          "factor": 1.1
        }
      },
      "order": "asc"
    }
  }
}
#script
GET product/_search
{
  "sort": [
    {
      "time": {
        "order": "asc"
      }
    },
    {
      "_script": {
        "type": "number",
        "script": {
          "lang": "painless",
          "source": "doc['price'].value * params.factor",
          "params": {
            "factor": 1.1
          }
        },
        "order": "asc"
      }
    }
  ]
}
```

## 二、超时机制

```json
GET product/_search
{
  "timeout": "1s"
}
```

默认没有timeout，如果设置了timeout，那么会执行timeout机制。
Timeout机制：假设用户查询结果有1W条数据，但是需要10s才能查询完毕，但是用户设置了1s的timeout，那么不管当前一共查询到了多少数据，都会在1s后ES将停止查询，并返回当前数据。

## 三、组合查询

bool把各种其它查询通过must（必须 ）、 must_not（必须不）、 should（应该）的方式进行组合。

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

  可能包含，返回的文档可能满足should子句的条件。在一个Bool查询中，如果没有must或者filter，有一个或者多个should子句，那么只要满足一个就可以返回。如果存在must或者filter，那么should中可满足>=0。minimum_should_match=0

- filter

  过滤，返回的文档必须满足filter子句的条件。但是不会像Must一样参与计算分值。filter不会计算score，并且结果是会被缓存的。

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

### 示例

```json
GET /product/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "name": "xiaomi" #name必须包含xiaomi
          }
        }
      ],
      "must_not": [
        {
          "match": {
            "name": "erji" #name必须不含有erji
          }
        }
      ],
      "should": [
        {
          "match": {
            "desc": "nfc" #desc中包含nfc，至少0个满足
          }
        }
      ],
      "filter": [
        {
          "range": {
            "price": {
              "gt": 4999
            }
          }
        }
      ]
    }
  }
}
```

## 四、范围查询

查询找出那些落在指定区间内的数字或者时间。 range 查询允许以下字符

![](images/range-1.png)

```json
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

## 五、分页查询

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

- Deep pageing深度分页问题

  1.当数据超过1W，不要使用

  2.返回结果不要超过1000个，500以下为宜

  3.尽量避免深度分页查询

  4.使用游标查询Scroll search（只能下一页，没办法上一页，不适合实时查询）

  ![img](images/深度分页.png)

  数据分散在不同的分片中，当分页要查询1-500的数据时，每个分片都要查出500个数据，总共是2500，然后再整体进行查询，取1-500。涉及到数据的聚合。会导致性能下降特别多。如果分页数量较少，还可以，返回结果如果太多，影响较大。

  使用scrollSearch 滚动搜索，每次执行，都会返回一个id。查询的时候，使用id进行查询，就会查下一页，缺点是无法查询上一页。

- 游标滚动查询

  相当于mysql中生成快照的方式，所以如果在游标查询期间有增删改操作，是获取不到最新的数据的。在过期时间内，之后的查询的scroll_id是不变的。

  ```json
  GET product/_search?scroll=1m //过期时间为1分钟
  {
    "query": {
      "match_all": {}
    },
    "size": 2
  }
  
  GET _search/scroll
  {
    "scroll":"1m",
    "scroll_id":"第一次查询返回的_scroll_id"
  }
  ```

  返回结果

  ![](images/滚动查询.png)


## 六、模糊查询

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

## 七、聚合查询

### 基本概念

- 聚合允许使用者对 es 文档进行统计分析，类似关系型数据库中的 group by，也有很多其他的聚合，例如取最大值、平均值等等。

- 聚合使用aggs关键字，使用"size": 0，可以不返回原始数据

- 用于进行聚合的字段必须是exact value，分词字段不可进行聚合

- 聚合分析的字段如果是text类型，一定打开doc value创建正排索引，否则打开fielddata =true(不推荐)。

- bucket：桶

- metirc：度量（单位、数量）

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

  ![image-20210808220509565](images/aggs-2.png)

### 聚合分类

1. 分桶聚合（Bucket agregations）：类比SQL中的group by的作用，主要用于统计不同类型数据的数量
2. 指标聚合（Metrics agregations）：主要用于最大值、最小值、平均值、字段之和等指标的统计
3. 管道聚合（Pipeline agregations）：用于对聚合的结果进行二次聚合，如要统计绑定数量最多的标签bucket，就是要先按照标签进行分桶，再在分桶的结果上计算最大值。

### 聚合排序

- 排序

  order可以指定排序字段，也可以指定嵌套的aggs聚合查询的name进行排序。
  _key：按每个桶的键值数值排序，字符串值的字母顺序排序，只在 terms 内使用。

  ```json
  GET product/_search?size=0
  {
    "aggs": {
      "tag_avg_price": {
        "terms": {
          "field": "tags.keyword",
          "order": {
            "agg_price": "asc"
          }
        },
        "aggs": {
          "agg_price": {
            "avg": {
              "field": "price"
            }
          }
        }
      }
    }
  }
  ```

  ![image-20210815161143574](images/aggs-order1.png)

- 多层排序

  按照多层聚合中的里层某个聚合的结果进行排序

  ```json
  #多个aggs的name进行排序，一级、二级。以里层聚合的sum值进行外层排序
  "terms": {
          "field": "tags.keyword",
          "order": {
            "agg_stats>stats.sum": "desc"
          }
        },
```
  
  ```json
  GET product/_search
  {
    "size": 0,
    "aggs": {
      "tag_avg_price": {
        "terms": {
          "field": "tags.keyword",
          "order": {
            "agg_stats>stats.sum": "desc" #以里层stats的sum值进行外层排序
          }
        },
        "aggs": {
          "agg_stats": {
            "filter": {
              "terms": {
                "type.keyword": [
                  "耳机","手机","电视"
                ]
              }
            },
            "aggs": {
              "stats": {
                "extended_stats": {
                  "field": "price"
                }
              }
            }
          }
        }
      }
    }
  }
```
  
extended_stats会把所有的计算结果进行展示，比如sum、avg、max等等
  
  ![image-20210815174758474](images/aggs-order2.png)

### global

- 默认的aggs聚合查询，是受到外层的查询结果的影响。

- 当aggs聚合查询内部嵌套了下钻分析，使用了global，则global层的查询，是不受外层影响的。

  ```json
  GET product/_search?size=0
  {
    "query": {
      "match": {
        "name": "手机"
      }
    },
    "aggs": {
      "avg_price1": {
        "avg": {
          "field": "price"
        }
      },
      "avg_price2": {
        "global": {}, 
        "aggs": {
          "avg_price2": {
            "avg": {
              "field": "price"
            }
          }
        }
      }
    }
  }
  ```

  外层的avg（avg_price1）聚合，受到了query的影响，计算的结果是在query结果基础上进行分析的。内部的global（avg_price2）是全局分析。不受外层影响。

  ![](images/aggs-global-1.png)


### histogram直方图或柱状图统计

柱状图聚合，可以设定间隔区间。

1. interval：设定间隔区间
2. min_doc_count：只展示结果大于等于1条的数据，可以过滤掉空数据。
3. keyed：展示bucket结果中，是key-value形式
4. missing：当某条记录是空置时，会给默认值
5. extended_bounds：当统计数据时，最大数据不到设定的最大值，会自动补充，并count为0；

```json
GET product/_search
{
  "size": 0,
  "aggs": {
    "price": {
      "histogram": {
        "field": "price",
        "interval": 1000,
        "min_doc_count": 0,
        "keyed": true,
        "missing": 4999,
        "extended_bounds": {
          "min": 0,
          "max": 10000
        }
      },
      "aggs": {
        "avg_price": {
          "avg": {
            "field": "price"
          }
        }
      }
    }
  }
}
```

![image-20210815172023702](images/histogram.png)

### date-histogram基于日期的直方图

时间格式的柱状图统计。format格式化

![](images/date-histogram.png)

### stats扩展

ES提供默认的聚合扩展实现，比如数值型，可以单独查询min、max、avg等计算结果，另外ES提供扩展stats关键字指令，可以默认提供一系列的默认实现。

- 数值类型

  ```json
  GET product/_search?size=0
  {
    "aggs": {
      "price": {
        "stats": {
          "field": "price"
        }
      }
    }
  }
  ```

  ![](images/数值-stats.png)

- string类型

  默认提供字符总数、最小长度、最大长度、平均长度等信息。

  entropy 熵值：基于聚合所收集到的所有项中每个字符出现的概率

  ```json
  GET product/_search?size=0
  {
    "aggs": {
      "desc": {
        "string_stats": {
          "field": "desc"
        }
      }
    }
  }
  ```

  ![image-20210815214526293](images/string-stats.png)

### cardinality

- 去重查询，并且几乎所有的aggs聚合查询，都支持script

- ES聚合查询是有1-6%的误差的，precision_threshold参数可以减小误差，但是会增加对内存的消耗。

- precision_threshold默认值为3000，最大值为4万，一般情况下不需要特意改变。设定值越大，精度越高。但是对内存的消耗也越大，内存消耗为precision_threshold * 8 个Byte，1000*8/1000 大概8kb。如果本身数据重复度就很低，没必要设置太大，去重查询是比较消耗资源的。

  ![image-20210815221155749](images/cardinality.png)

### top_hits

取前n条数据。按照type进行分组，按照每组数量的倒序排序，取前两个bucket（组）。并且每组内数据，按照价格倒序排序，取前两个组内明细数据。

```json
GET product/_search
{
  "size": 0,
  "aggs": {
    "top_tags": {
      "terms": {
        "field": "type.keyword",
        "size": 2,
        "order": {
          "_count": "desc"
        }
      },
      "aggs": {
        "top_type": {
          "top_hits": {
            "size": 2,
            "sort": [
              {
                "price": "desc"
              }
            ]
          }
        }
      }
    }
  }
}
```

### filters

- filers做聚合的时候，可以对除了指定的聚合条件进行聚合外，还可以对条件外的数据做other聚合。

- other_bucket：是否对剩余数据做聚合

- other_bucket_key：指定other的聚合的key

  ```json
  GET product/_search
  {
    "size": 0,
    "aggs": {
      "test_filters": {
        "filters": {
          "other_bucket": true,
          "other_bucket_key": "other_type",
          "filters": {
            "phone": {
              "term": {
                "type.keyword": "手机"
              }
            },
            "tv": {
              "term": {
                "type.keyword": "电视"
              }
            }
          }
        }
      }
    }
  }
  ```

  ![image-20210816214702491](images/filters-1.png)

### 深度优先、广度优先

- terms桶聚合，ES基于我们的数据动态进行聚合分组构建桶，但是es并不知道会构建出多少个桶，所以会把所有的数据都拿到内存中进行计算，大多数情况下单个字段的聚合速度还是可以的，但是如果多个字段进行聚合，就会导致构建大量的组，最终有可能导致发生OOM问题。

  比如1万个演员，每个演员演了10部电影，要聚合演员点赞数前10的演员、并且演员点赞数前10的电影，就需要聚合1万+1万*10个组合，如果聚合的深度再多一层，聚合的组合数据量就会更大。这就会很容易导致jvm问题。
- 深度优先：先聚合所有数据，构建一颗完整的树，再进行数据筛选、剔除无用数据。
- 广度优先：先构建第一层的聚合结果，剔除无用数据，再进行下一层数据的聚合，就会减少内存的消耗，提升速度。但是广度优先只适用于每个组聚合数量远小于组总数的情况。
- terms.collect_mode属性用于设置广度优先还是深度优先，默认是深度优先（depth_first），广度优先则修改为breadth_first。

### adjacency_matrix邻接矩阵

- 对每个聚合结果单独进行展示，并且会对每个聚合结果进行聚合&运算聚合。比如聚合A、B、C，除了展示A、B、C条件的聚合结果外，还会进行A&B、A&C、B&C结果的聚合。

- A&B的含义是：doc既要匹配A条件的聚合又要匹配B条件的聚合。

  ```
  PUT /emails/_bulk?refresh
  {"index":{"_id":1}}
  {"accounts":["a","f"]}
  {"index":{"_id":2}}
  {"accounts":["a","b"]}
  {"index":{"_id":3}}
  {"accounts":["c","b"]}
  ```

  ![image-20210816225430405](images/邻接矩阵.png)

## 八、批量查询

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

## 九、脚本script

Scripting是Elasticsearch支持的一种专门用于复杂场景下支持自定义编程的强大的脚本功能，ES支持多种脚本语言，如painless，其语法类似于Java，也有注释、关键字、类型、变量、函数等，相对于其他脚本高出几倍的性能，并且安全可靠，可以用于内联和存储脚本。

### update更新修改

```json
#语法：ctx._source.<field-name>
POST product/_update/2
{
  "script": {
    "source": "ctx._source.price-=1"
  }
}
POST product/_update/6
{
  "script": {
    "lang": "painless",
    "source": "ctx._source.tags.add('无线充电')"
  }
}
```

### delete删除

```json
POST product/_update/10
{
  "script": {
    "lang": "painless",
    "source": "ctx.op='delete'"
  }
}
```

### search查询

查询时使用painless，source指定具体字段，使用doc['filedName'].value，通过params可以定义参数

```json
GET product/_search
{
  "script_fields": {
    "price_query": {
      "script": {
        "lang": "painless",
        "source": "doc['price'].value+params.num",#通过params.num可以设置参数
        "params": {
          "num": 2
        }
      }
    }
  }
}
```

复杂查询示例

```json
GET product/_search
{
  "script_fields": {
    "price": { #第一层，查询出来价格
      "script": {
        "lang": "painless",
        "source": "doc['price'].value"
      }
    },
    "discount_price": {
      "script": { #第二层，基于第一层的查询结果，进行不同系数的价格计算
        "lang": "painless",
        "source": "[doc['price'].value* params.discount_8,doc['price'].value* params.discount_7,doc['price'].value* params.discount_6,doc['price'].value* params.discount_5]",
        "params": {
          "discount_8": 0.8,
          "discount_7": 0.7,
          "discount_6": 0.6,
          "discount_5": 0.5
        }
      }
    }
  }
}
```

![](images/script_复杂01.png)

### scripts模板

scripts脚本可以存储成模板，这样可以直接调用，否则每次查询时，都需要对脚本进行解析，存储成模板之后，方便不同的查询语句使用，也可以提升查询效率。

使用的时候，用id即可

```json
POST _scripts/calculate_discount
{
  "script": {
    "lang": "painless",
    "source": "doc.price.value * params.discount"
  }
}
#查看
GET _scripts/calculate_discount
#查询时使用
GET product/_search
{
  "script_fields": {
    "price": {
      "script": {
        "id": "calculate_discount",
        "params": {
          "discount": 0.8
        }
      }
    }
  }
}
```

### Scripting的函数式编程

当需要在script中使用复杂脚本时，需要用"""脚本"""格式，在脚本的前后各有三个双引号。

```json
#更新
POST product/_update/1
{
  "script": {
    "lang": "painless",
    "source": """
      ctx._source.tags.add(params.tag_name);
      ctx._source.price-=100;
    """,
    "params": {
      "tag_name": "无线秒充1"
    }
  }
}
POST product/_update/3
{
  "script": {
    "lang": "painless",
    "source": """
      if(ctx._source.name ==~ /[\s\S]*小米[\s\S]*/) {
        ctx._source.name+="***|"
      }else{
        ctx.op="noop"
      }
    """
  }
}
#查询
GET product/_search
{
  "query": {
    "constant_score": {
      "filter": {
        "range": {
          "price": {
            "lte": 1000
          }
        }
      }
    }
  },
  "aggs": {
    "tag_agg": {
      "sum": {
        "script": {
          "lang": "painless",
          "source": """
            int total = 0;
            for(int i = 0; i <doc['tags.keyword'].length; i++){
              total++;
            }
            return total;
          """
        }
      }
    }
  }
}
```

## 十、multi_match多字段搜索

multi_match 与 match 类似，不同的是它可以在多个字段中查询。

```json
GET score/_search
{
  "query": {
    "multi_match": {
      "query": "超级快充",
      "fields": ["name","desc"],
      "type": "most_fields",
      "tie_breaker": 0.3
    }
  }
}
```

multi_match的匹配策略包括best_fields、most_fields、cross_fields，默认的是best_fields。

- best_fields

  默认的搜索策略。对于同一个query，单个field匹配更多的term，则优先排序。当查询多个字段时，如果关键词在某个字段中被匹配的次数比较多，则这个字段是best_ields，最好的字段。其余字段匹配的相关性分数就会被忽略，这个词条的相关性分数就会以best_fields为准。

- most_fields

  如果一次请求中，对于同一个doc，匹配到某个term的field越多，则越优先排序。例如doc1，四个字段匹配到了关键词，doc2是3个字段匹配到了关键词。那么doc1优先展示。因为匹配的field最多。

- cross_fields

  cross_fields交叉的字段。含义是关键词分词之后，每个关键字，必须在其中至少一个字段中匹配。

  比如“吴磊”，使用cross_fields策略，会产生俩个条件:
  
  1、姓或者名中,必须有吴
  2、姓或者名中,必须有磊
  
  默认是或操作。满足一条就行,存在吴,或者磊即可。可以使用and操作,只有两条都满足,存在完整的吴磊，才可以。
  
  ![](images/cross-1.png)
  

## 十一、dis_max

```json
GET product/_search
{
  "query": {
    "dis_max": {
      "tie_breaker": 0.7,
      "boost": 1.2,
      "queries": []
    }
  }
}
```

dis_max查询（Disjunction Max Query）：将任何与任一查询匹配的文档作为结果返回，但只将最佳匹配的评分作为查询的评分结果返回。比如查询时name、desc两个字段，name的评分结果比desc高，则以name的评分为准进行返回，desc字段的评分将被忽略。

dix_max默认的best_fields策略，会带来一个问题，当搜索词在fields字段中全部存在时，只会以其中一个字段的评分为准，当doc1只匹配一个字段，但是评分高，doc2匹配的2个字段，正常情况下，匹配2个字段的应该优先展示，但是因为best_fields策略，导致其余字段不参与评分，最终结果不准确。所以可以使用tie_breaker设置其余字段的参与度，官方建议0.1-0.4之间。太大的话，有可能导致喧宾夺主。

- tie_breaker

  取值范围 [0,1]，其中 0 代表使用 dis_max 最佳匹配语句的普通逻辑，1表示所有匹配语句同等重要。最佳的精确值需要根据数据与查询调试得出，但是合理值应该与零接近（处于 0.1 - 0.4 之间），这样就不会颠覆 dis_max 最佳匹配性质的根本。

```json
GET score/_search
{
  "query": {
    "dis_max": {
      "queries": [
        {
          "match": {
            "name": "超级快充"
          }
        },
        {
          "match": {
            "desc": "超级快充"
          }
        }
      ],
      "tie_breaker": 0.3
    }
  }
}
```

## 十二、Nested Search复杂类型查询

### 1、基本概念

- nested类型是object数据类型的专用版本，它允许以可以彼此独立地查询对象的方式对对象数组进行索引，当存储内部对象为复杂类型时应该使用nested而不是object。

- 默认的object类型，会将复杂类型所有数据中相同字段的值创建为同一个索引。

  ```
  PUT /order/_doc/1
  {
  	"order_name": "小米10 Pro订单",
  	"desc": "shouji zhong de zhandouji",
  	"goods_count": 3,
  	"total_price": 12699,
  	"goods_list": [
  		{
  			"name": "小米10 PRO MAX 5G",
  			"price": 4999
  		},
  		{
  			"name": "钢化膜",
  			"price": 19
  		},
  		{
  			"name": "手机壳",
  			"price": 199
  		}
  	]
  }
  ```

  虽然goods_list存在三条数据，但是在创建索引时，会将所有的name、price统一创建，实际应该按照三条数据进行分解查找，但是最终会汇聚成一条。

  ```json
  #三条数据变成了一条
  {
  	"name": [
  		"小米10",
  		"PRO",
  		"MAX",
  		"5G",
  		"钢化膜",
  		"手机壳"
  	],
  	"price": [
  		4999,
  		19,
  		199
  	]
  }
  ```

- 判断数据格式是嵌套类型，创建mapping时，字段类型应该指定为nested。如果不指定，默认会创建为object类型。导致按照条件查询时结果错误。

### 2、查询语法以及mapping

- 查询时要使用nested查询，path为nested嵌套的级别，nested对象的查询深度。query为子查询。

  ```json
  {
    "query": {
      "nested": {
        "path": "path_to_nested_doc",
        "query": {}
      }
    }
  }
  ```

- 创建mapping示例

  ```json
  {
  	"mappings": {
  		"properties": {
  			"goods_list": { #嵌套类型
  				"type": "nested",
  				"properties": {
  					"name": {
  						"type": "text",
  						"fields": {
  							"keyword": {
  								"type": "keyword",
  								"ignore_above": 256
  							}
  						},
  						"analyzer": "ik_max_word"
  					},
  					"price": {
  						"type": "long"
  					}
  				}
  			}
  		}
  	}
  }
  ```

  创建mapping时，goods_list为nested类型，所以要查询goods_list数据时，要使用nested，并且path使用goods_list。

  正确的查询结果

  ![image-20210815101508207](images/nested-1.png)

  如果不使用nested类型，查询时将会匹配goods_list所有的数据，并不只是按照每条单独匹配。

### 3、复杂查询示例

- 复杂的嵌套查询示例

  创建mapping

  ```json
  PUT /area
  {
    "mappings": {
      "properties": {
        "province": {
          "type": "nested",
          "properties": {
            "name": {
              "type": "text",
              "analyzer": "ik_max_word"
            },
            "cities": {
              "type": "nested",
              "properties": {
                "name": {
                  "type": "text",
                  "analyzer": "ik_max_word"
                },
                "district": {
                  "type": "nested",
                  "properties": {
                    "name": {
                      "type": "text",
                      "analyzer": "ik_max_word"
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
  ```

  插入数据

  ```json
  PUT /area/_doc/1
  {
    "province": {
      "name": "北京",
      "cities": [
        {
          "name": "北京市",
          "district": [
            {"name":"丰台区"},
            {"name":"海淀区"},
            {"name":"朝阳区"},
            {"name":"东城区"},
            {"name":"西城区"},
            {"name":"昌平区"}
            ]
        }
      ]
    }
  }
  PUT /area/_doc/2
  {
    "province": {
      "name": "河南省",
      "cities": [
        {
          "name": "郑州市",
          "district": [
            {"name":"金水区"},
            {"name":"高新区"},
            {"name":"郑东新区"},
            {"name":"二七区"},
            {"name":"中原区"},
            {"name":"惠济区"}
            ]
        },
         {
          "name": "鹤壁市",
          "district": [
            {"name":"山城区"},
            {"name":"淇滨区"},
            {"name":"鹤山区"},
            {"name":"朝歌"},
            {"name":"浚县"}
            ]
        }
      ]
    }
  }
  PUT /area/_doc/3
  {
    "province": {
      "name": "台湾省",
      "cities": [
        {
          "name": "台北市",
          "district": [
            {"name":"中正区"},
            {"name":"大同区"},
            {"name":"中山区"},
            {"name":"万华区"},
            {"name":"信义区"},
            {"name":"松山区"}
            ]
        },
         {
          "name": "高雄",
          "district": [
            {"name":"小港区"},
            {"name":"鼓山区"},
            {"name":"三民区"}
            ]
        }
      ]
    }
  }
  ```

  查询示例

  ```json
  #city为包含北京市 或者 包含淇滨区的省份信息
  GET area/_search
  {
    "query": {
      "nested": {
        "path": "province",
        "query": {
          "nested": {
            "path": "province.cities",
            "query": {
              "bool": {
                "should": [
                  {
                    "term": {
                      "province.cities.name": "北京"
                    }
                  },
                  {
                    "nested": {
                      "path": "province.cities.district",
                      "query": {
                        "term": {
                          "province.cities.district.name": "淇滨区"
                        }
                      }
                    }
                  }
                ]
              }
            }
          }
        }
      }
    }
  }
  ```

### 4、score_mode

- score_mode：聚合分数计算方式

  ```json
  {
    "query": {
      "nested": {
        "path": "path_to_nested_doc",
        "query": {},
        "score_mode": "avg"
      }
    }
  }
  ```

  1.	avg （默认）：使用所有匹配的子对象的平均相关性得分。
  2.	max：使用所有匹配的子对象中的最高相关性得分。
  3.	min：使用所有匹配的子对象中最低的相关性得分。
  4.	none：不要使用匹配的子对象的相关性分数。该查询为父文档分配得分为0。
  5.	sum：将所有匹配的子对象的相关性得分相加。

## 十三、Join查询

### 基本概念

- 在同一索引的文档中创建父/子关系

### 创建mapping

- 创建了一个字段，名称为my_join_field，type为join。指定上下级关系，上级为depart，下级为employee

  ```json
  PUT join_test
  {
    "mappings": {
      "properties": {
        "my_join_field": {
          "type": "join",
          "relations": {
            "depart": "employee"
          }
        }
      }
    }
  }
  ```

### 创建数据

- 创建父级的时候，join字段内要指明当前数据的join name。用于限定当前数据是否为父级。

- 创建子级数据的时候，除了要指定join name，还要指定routing=1(父级id)，要保证子级和父级在同一分片内。并且还要指定parent的id是哪个。

  ```json
  PUT join_test/_doc/1
  {
    "name": "行政部",
    "my_join_field": {
      "name": "depart"
    }
  }
  PUT join_test/_doc/2
  {
    "name": "财务部",
    "my_join_field": {
      "name": "depart"
    }
  }
  PUT join_test/_doc/3
  {
    "name": "研发部",
    "my_join_field": {
      "name": "depart"
    }
  }
  
  POST join_test/_doc?routing=1?refresh
  {
    "name": "张一",
    "my_join_field": {
      "name": "employee",
      "parent": "1"
    }
  }
  POST join_test/_doc?routing=1?refresh
  {
    "name": "张二",
    "my_join_field": {
      "name": "employee",
      "parent": "2"
    }
  }
  POST join_test/_doc?routing=1?refresh
  {
    "name": "张三",
    "my_join_field": {
      "name": "employee",
      "parent": "2"
    }
  }
  ```

### 数据查询

1. 查询父级数据

   使用has_child关键字进行查询，type指定子级的类型。并且只能查询出存在子级的父级数据。

   ```json
   GET join_test/_search
   {
     "query": {
       "has_child": {
         "type": "employee",
         "query": {
           "match_all": {}
         }
       }
     }
   }
   ```

2. 查询子级数据

   查询子级数据时，要使用has_parent关键字，并且parent_type指定为父级的类型。

   ```json
   GET join_test/_search
   {
     "query": {
       "has_parent": {
         "parent_type": "depart",
         "query": {
           "match_all": {}
         }
       }
     }
   }
   //指定id查询
   GET join_test/_search
   {
     "query": {
       "parent_id": {
         "type": "employee",
         "id": 2
       }
     },
     "aggs": {
       "conut": {
         "terms": {
           "field": "my_join_field"
         }
       }
     }
   }
   ```

## 十四、高亮查询

- 查询结果中关键词高亮显示，一般使用默认配置即可。

- 三种高亮

  1. unified ：默认的高亮方式，使用Lucene的实现方式。
  2. plain ：性能较高，消耗少量内存，性价比高。
  3. fvh：fast vactor highlighter 适合字段较大，较复杂的查询情况，字段需要设置为vactor类型。

  常用plain即可。

- 自定义标签

  1. pre_tag：起始标签
  2. post_tag：结束标签
  3. 每个高亮字段都需要对应一个查询

  ```json
  GET news/_search
  {
    "query": {
      "bool": {
        "should": [
          {
            "match": {
              "title": "baoqiang"
            }
          },
          {
            "match": {
              "name": "baoqiang"
            }
          }
        ]
      }
    },
    "highlight": {
      "fields": {
        "title": {
          "pre_tags": "<b>",
          "post_tags": "</b>",
          "type": "unified"
        },
        "name": {
          "pre_tags": "<b>",
          "post_tags": "</b>",
          "type": "unified"
        }
      }
    }
  }
  ```

  ![](images/high-light-.png)

