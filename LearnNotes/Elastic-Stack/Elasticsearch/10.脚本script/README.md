# Elasticsearch

## 脚本script

- Painless脚本语言，类似Java语法，是ES支持的默认的脚本语言，其他的还支持expression、mustache、java，但是支持的不是很好，有限制。

- ES首次执行脚本的时候，会对脚本进行编译并缓存。所以脚本中使用到的数据最好定义成变量，放到params中。这样不会影响到编译缓存，否则每次修改，ES都会重新编译。

- 更新：ctx._source默认语法，代表获取元数据。支持params定义参数。

  ```json
  POST product/_update/1
  {
    "script": {
      "lang": "painless",
      "source": "ctx._source.price += params.num",
      "params": {
        "num": 2
      }
    }
  }
  ```

- upsert：数据存在时执行更新，不存在时，执行插入操作

  ```json
  POST product2/_update/4
  {
    "script": {//数据存在时，执行此处的更新操作
      "lang": "painless",
      "source": "ctx._source.price -= params.sum",
      "params": {
        "sum":150
      }
    },
    "upsert": {//数据不存在时，执行此处的create操作，将数据插入
      "name":"xiaomi zhineng phone",
      "desc":"xingjiabi zhineng shouji"
    }
  }
  ```

- 代码块

  painless代码格式，和Java类似，代码放在"""代码"""，左右均为三个双引号。

  ```json
  POST product2/_update/1
  {
    "script": {
      "lang": "painless",
      "source": """  //代码块
        ctx._source.name += params.name;
        ctx._source.price -= 1
      """,
      "params": {
        "name": "无线充电",
        "price": "1"
      }
    }
  }
  ```

- 查询时使用painless，source指定具体字段，使用doc['filedName'].value，text文本需要用keyword

  ```json
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

- 删除

  ```json
  POST product2/_update/4
  {
    "script": {
      "lang": "painless",
      "source": "ctx.op='delete'"
    }
  }
  ```

- 复杂查询

  获取元数据信息，使用doc关键字，将导致该字段的条件被加载到内存（缓存），这将导致更快的执行，但会消耗更多的内存。此外，doc[...]符号只允许简单类型（不能返回一个复杂类型(JSON对象或者nested类型)），只有在非分析或单个词条的基础上有意义。如果可能，使用doc还是访问文档的推荐方式，因为_source每次使用时都必须加载并解析。使用_source非常缓慢。

  ```
  获取元数据的固定写法
  params['_source']['price']
  或者换成
  doc['price'].value
  ```

  ```json
  GET /product/_search
  {
    "aggs": {
      "sum_person": {
        "sum": {
          "script": {
            "lang": "painless",
            "source": """
              int total = 0;
              for (int i = 0; i < params['_source']['tags'].length; i++)
              {
                if (params['_source']['tags'][i] == 'xingjiabi') {
                  total += 1;
                }
              }
              return total;
            """
          }
        }
      }
    },
    "size": 0
  }
  ```

  ```json
  GET /product/_search
  {
    "aggs": {
      "sum_person": {
        "sum": {
          "script": {
            "lang": "painless",
            "source": """
              int total = 0;
              if (params['_source']['price'] <=1000) {
                  total += 1;
                }
              return total;
            """
          }
        }
      }
    },
    "size": 0
  }
  ```

- Dates

  ZonedDateTime类型，因此它们支持诸如之类的方法getYear，getDayOfWeek 或例如从历元开始到毫秒getMillis。要在脚本中使用它们，请省略get前缀并继续使用小写的方法名其余部分。

  ```json
  #getMonth()
  #getDayOfMonth()
  #getDayOfWeek()
  #getDayOfYear()
  #getHour()
  #getMinute()
  #getSecond()
  #getNano()
  GET product2/_search/
  {
    "query": {
      "term": {
        "_id": {
          "value": "1"
        }
      }
    }, 
    "script_fields": {
      "test": {
        "script": {
          "source": "doc.createtime.value.dayOfYear"
        }
      }
    }
  }
  ```

- 

## Stored scripts

- 可以理解为script模板  缓存在集群的cache中。

- 作用域为整个集群。默认缓存大小是100MB  没有过期时间，可以手工设置过期时间script.cache.expire，通过script.cache.max_size设置缓存大小，脚本最大64MB，通过script.max_size_in_bytes配置，只有发生变更时重新编译。

  ```json
  /_scripts/{id}//定义模板
  PUT _scripts/price-dis
  {
    "script":{
      "lang": "painless",
      "source": "doc['price'].value - params.sum"
    }
  }
  GET _scripts/price-dis
  GET product2/_search//目前只适合查询
  {
    "script_fields": {
      "price": {
        "script": {
          "id": "price-dis",
          "params": {
            "sum": 150
          }
        }
      }
    }
  }
  ```

- 