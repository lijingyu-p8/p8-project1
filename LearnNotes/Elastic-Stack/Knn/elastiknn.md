# API手册

## 一、settings

```json
PUT /my-index
{
  "settings": {
    "index": {
      "number_of_shards": 1,          # 1  
      "elastiknn": true               # 2
    }
  }
}
```

1. 索引中的分片数。与所有Elasticsearch查询一样，Elastiknn查询在每个碎片上并行执行一次。这意味着您通常可以通过向索引中添加更多碎片来加快查询速度。
2. 在Elasticsearch 7.7.x及以上版上，将此设置为（默认值为false）true会显著提高Elastiknn的性能。因为Elastiknn将向量存储为二进制文档值。将此设置为true，Elastiknn使用非默认Lucene设置来存储文档值。默认设置可以节省磁盘空间，但会使读取向量的速度大大降低。如果确实需要节省磁盘空间或需要冻结索引，则应将其设置为false。

## 二、mapping

在索引向量之前，首先定义一个映射，指定向量数据类型、索引模型和模型参数。这决定了索引向量支持哪些查询

```json
PUT /my-index/_mapping
{
  "properties": {                               # 1
    "my_vec": {                                 # 2 
      "type": "elastiknn_sparse_bool_vector",   # 3
      "elastiknn": {                            # 4
        "dims": 100,                            # 5
        "model": "exact",                       # 6
        ...                                     # 7
      }
    }
  }
}
```

1. mapping API关键字。
2. 包含向量的字段的名称。这是任意的，可以嵌套在其他字段下。
3. 要存储的向量类型。
4. elastiknn相关设置的API关键字。
5. 向量的维数。存储在此字段中的所有向量必须具有相同的维度。
6. 模型类型。模型类型将决定可以运行哪种类型的搜索。
7. 其他模型参数。请参见下面的模型。

### elastiknn_sparse_bool_vector

该类型针对每个索引为true或false且大多数索引为false的向量进行了优化。例如，您可以表示一个文档的编码单词包，其中每个索引对应于词汇表中的一个单词，而任何单个文档都只包含一小部分单词。在内部，Elastiknn仅通过存储真实索引来节省空间。

```json
POST /my-index/_doc
{
    "my_vec": {
       "true_indices": [1, 3, 5, ...],   # 1
       "total_indices": 100,             # 2
    }
}
```

```json
POST /my-index/_doc
{
    "my_vec": [[1, 3, 5, ...], 100]      # 3
}
```

1. vector向量维度
2. dims 总共有多少维度
3. 整合在一起的简要写法

### elastiknn_dense_float_vector

这种类型针对每个索引为浮点数的向量进行了优化，所有索引都被填充，维度通常不超过1000。例如，您可以存储单词嵌入或图像向量。在内部，Elastiknn使用Java float来存储值。

```json
POST /my-index/_doc
{
    "my_vec": {
        "values": [0.1, 0.2, 0.3, ...]    # 1
    }
}
```

## 三、model搜索模型类型

### 3.1、exact

```json
PUT /my-index/_mapping
{
    "properties": {
        "my_vec": {
            "type": "elastiknn_(dense_float | sparse_bool)_vector",  # 1
            "elastiknn": {
                "dims": 100,                                         # 2
            }
        }
    }
}
```

- 精确的模型将允许运行精确的搜索。它们不利用任何索引构造，时间复杂度为O(n^2)。

- 支持dense（密集）、sparse（稀疏）
- 默认即为exact精确搜索

### 3.2、Jaccard LSH

```json
PUT /my-index/_mapping
{
    "properties": {
        "my_vec": {
            "type": "elastiknn_sparse_bool_vector", # 1
            "elastiknn": {
                "dims": 25000,                      # 2
                "model": "lsh",                     
                "similarity": "jaccard",            # 3
                "L": 99,                            # 4
                "k": 1                              # 5
            }
        }
    }
}
```

使用Minhash算法散列和存储稀疏布尔向量，以便它们支持近似的Jaccard相似性查询。

1. 只支持sparse稀疏类型
2. 维度最大支持25000
3. 配置相似性函数
4. 哈希表的数量。通常，增加此值会增加召回率。
5. 组合单个哈希值的哈希函数数量。通常，增加此值会提高精度。

### 3.3、Hamming LSH

```json
PUT /my-index/_mapping
{
    "properties": {
        "my_vec": {
            "type": "elastiknn_sparse_bool_vector", # 1
            "elastiknn": {
                "dims": 25000,                      # 2
                "model": "lsh",                     
                "similarity": "hamming",            # 3
                "L": 99,                            # 4
                "k": 2                              # 5
            }
        }
    }
}
```

使用Bit-Sampling algorithm 位采样算法散列和存储稀疏布尔向量，以便它们支持近似的汉明相似性查询。

与规范位采样方法的唯一区别在于，它对k位进行采样并组合，以形成单个哈希值。例如，如果设置L=100，k=3，它将从向量中采样100*3=300位，并串联3位的集合以形成每个哈希值，总共100个哈希值。

1. 只支持sparse稀疏类型
2. 维度最大支持25000
3. 配置相似性函数
4. 哈希表的数量。通常，增加此值会增加召回率。
5. 组合单个哈希值的哈希函数数量。通常，增加此值会提高精度。

### 3.4、Cosine 余弦 LSH

```json
PUT /my-index/_mapping
{
    "properties": {
        "my_vec": {
            "type": "elastiknn_dense_float_vector", # 1
            "elastiknn": {
                "dims": 1000,                       # 2
                "model": "lsh",                     
                "similarity": "cosine",             # 3
                "L": 99,                            # 4
                "k": 1                              # 5
            }
        }
    }
}
```

使用Random Projection algorithm随机投影算法散列和存储密集浮点向量，以便它们支持近似余弦相似性查询。

1. 只支持dense密集类型
2. 维度最大支持1000
3. 配置相似性函数
4. 哈希表的数量。通常，增加此值会增加召回率。
5. 组合单个哈希值的哈希函数数量。通常，增加此值会提高精度。

### 3.5、L2（欧几里得） LSH

```json
PUT /my-index/_mapping
{
    "properties": {
        "my_vec": {
            "type": "elastiknn_dense_float_vector", # 1
            "elastiknn": {
                "dims": 100,                        # 2
                "model": "lsh",                     
                "similarity": "l2",                 # 3
                "L": 99,                            # 4
                "k": 1,                             # 5
                "w": 3                              # 6
            }
        }
    }
}
```

使用Stable Distributions method 稳定分布方法对密集浮点型向量进行哈希处理和存储，以便它们支持近似 L2（欧几里得）相似性查询

1. 只支持dense密集类型
2. 维度最大支持1000
3. 配置相似性函数
4. 哈希表的数量。通常，增加此值会增加召回率。
5. 组合单个哈希值的哈希函数数量。通常，增加此值会提高精度。
6. bucket 桶的宽度，为整数。这决定了两个向量的接近程度。典型值为低位整数。

### 3.6、Permutation LSH

```json
PUT /my-index/_mapping
{
    "properties": {
        "my_vec": {
            "type": "elastiknn_dense_float_vector", # 1
            "elastiknn": {
                "dims": 100,                        # 2
                "model": "permutation_lsh",         # 3
                "k": 10,                            # 4
                "repeating": true                   # 5
            }
        }
    }
}
```

## 四、查询

Elastiknn 提供了一个名为elastiknn_nearest_neighbors 的查询，它可以像标准 Elasticsearch 查询一样在请求中使用，也可以与标准的 Elasticsearch 查询结合使用。

```json
GET /my-index/_search
{
    "query": {
        "elastiknn_nearest_neighbors": {        # 1
            "field": "my_vec",                  # 2
            "vec": {                            # 3
                "values": [0.1, 0.2, 0.3, ...],               
            },
            "model": "exact",                   # 4
            "similarity": "cosine",             # 5
            ...                                 # 6
        }
    }
}
```

### 4.1、查询向量

```json
GET /my-index/_search
{
    "query": {
        "elastiknn_nearest_neighbors": {    
            ...
            "vec": {                                # 1
                "true_indices": [1, 3, 5, ...],
                "total_indices": 1000
            },
            ...
            "vec": {                                # 2
                "values": [0.1, 0.2, 0.3, ...]
            },
            ...
            "vec": {                                # 3
                "index": "my-other-index",
                "field": "my_vec",
                "id": "abc123"
            },
        }
    }
}
```

1. sparse bool 稀疏类型的查询
2. dense float密集浮点型的查询
3. 索引查询向量。这假设调用了另一个索引my-other-index，该索引具有 id 的文档，该文档在my_vec字段中包含有效的向量。

### 4.2、精确查询

```json
GET /my-index/_search
{
    "query": {
        "elastiknn_nearest_neighbors": {        
            "field": "my_vec", 
            "vec": {                                # 1
                "values": [0.1, 0.2, 0.3, ...],
            },
            "model": "exact",                       # 2
            "similarity": "(cosine | l1 | l2)",    # 3
        }
    }
}
```

1. 必须与my_vec的数据类型匹配，或是指向与该类型匹配的索引向量的指针。
2. 模型名称
3. 相似性函数。必须与矢量类型兼容。

### 4.3、LSH搜索策略

所有LSH搜索模型都遵循大致相同的策略。它们首先基于公共哈希项检索近似邻居，然后计算最佳近似候选子集的精确相似度。具体步骤如下：

1. 使用索引向量映射中指定的模型参数对查询向量进行散列。
2. 使用哈希值构造并执行查询，以查找具有相同哈希值的其他向量。该查询是对Lucene的TermInSetQuery的修改。
3. 获取最匹配哈希的向量，并计算它们与查询向量的精确相似度。candidates 参数控制着精确相似度计算的次数。具体来说，我们计算每个片段中的顶级candidates 候选向量的精确相似度。提醒一下，每个Elasticsearch索引有>=1个shards，每个shards有>=1个segments分段。这意味着，如果您为具有 2 个分片（每个分片有 3 个段）的索引进行设置"candiates": 200，则您将计算向量的确切相似性2 * 3 * 200 = 1200。 candidates必须设置为大于或等于要获取的 Elastic 搜索结果数的数字。值越高，通常意味着召回率越高，延迟越高。

### 4.4、Jaccard LSH Query

```json
GET /my-index/_search
{
    "query": {
        "elastiknn_nearest_neighbors": {
            "field": "my_vec",                     # 1
            "vec": {                               # 2
                "true_indices": [1, 3, 5, ...],
                "total_indices": 100
            },
            "model": "lsh",                        # 3
            "similarity": "jaccard",               # 4
            "candidates": 50                       # 5
        }
    }
}
```

### 4.5、Hamming LSH Query

```json
GET /my-index/_search
{
    "query": {
        "elastiknn_nearest_neighbors": {
            "field": "my_vec",                     # 1
            "vec": {                               # 2
                "true_indices": [1, 3, 5, ...],
                "total_indices": 100
            },
            "model": "lsh",                        # 3
            "similarity": "hamming",               # 4
            "candidates": 50                       # 5
        }
    }
}
```

### 4.6、Cosine LSH Query

```json
GET /my-index/_search
{
    "query": {
        "elastiknn_nearest_neighbors": {
            "field": "my_vec",                     # 1
            "vec": {                               # 2
                "values": [0.1, 0.2, 0.3, ...]
            },
            "model": "lsh",                        # 3
            "similarity": "cosine",                # 4
            "candidates": 50                       # 5
        }
    }
}
```

### 4.7、L2 LSH Query

```json
GET /my-index/_search
{
    "query": {
        "elastiknn_nearest_neighbors": {
            "field": "my_vec",                     # 1
            "vec": {                               # 2
                "values": [0.1, 0.2, 0.3, ...]
            },
            "model": "lsh",                        # 3
            "similarity": "l2",                    # 4
            "candidates": 50,                      # 5
            "probes": 2                            # 6
        }
    }
}
```

- probes：使用多探针搜索技术的探针数量。默认值为零。最大值为3^k，通常，增加探针probes会增加召回率，将允许您使用较小的L值和可比较的召回，但在查询时引入了一些额外的计算，有关L和k的更多信息，请参见L2 LSH映射部分。

### 4.8、Permutation LSH Query

```json
GET /my-index/_search
{
    "query": {
        "elastiknn_nearest_neighbors": {
            "field": "my_vec",                     # 1
            "vec": {                               # 2
                "values": [0.1, 0.2, 0.3, ...]
            },
            "model": "permutation_lsh",            # 3
            "similarity": "cosine",                # 4
            "candidates": 50                       # 5
        }
    }
}
```

## 五、模型和查询兼容性

一些模型可以支持多种类型的查询。例如，使用Jaccard LSH模型索引的稀疏布尔向量支持使用Jaccard和Hamming相似度进行精确搜索。相反的情况并非如此：使用精确模型exact存储的向量不支持Jaccard LSH查询。

- elastiknn_sparse_bool_vector

  | Model / Query                   | Exact    | Sparse Indexed | Jaccard LSH | Hamming LSH |
  | ------------------------------- | -------- | -------------- | ----------- | ----------- |
  | Exact (i.e. no model specified) | ✔ (J, H) | x              | x           | x           |
  | Sparse Indexed                  | ✔ (J, H) | ✔ (J, H)       | x           | x           |
  | Jaccard LSH                     | ✔ (J, H) | x              | ✔           | x           |
  | Hamming LSH                     | ✔ (J, H) | x              | x           | ✔           |

- elastiknn_dense_float_vector

  | Model / Query                   | Exact         | Cosine LSH | L2 LSH | Permutation LSH |
  | ------------------------------- | ------------- | ---------- | ------ | --------------- |
  | Exact (i.e. no model specified) | ✔ (C, L1, L2) | x          | x      | x               |
  | Cosine LSH                      | ✔ (C, L1, L2) | ✔          | x      | x               |
  | L2 LSH                          | ✔ (C, L1, L2) | x          | ✔      | x               |
  | Permutation LSH                 | ✔ (C, L1, L2) | x          | x      | ✔               |

## 六、常见模式

通常会根据某些属性筛选文档子集，然后在该子集上运行elastiknn_nearest_neighbors查询。例如，如果您的文档包含一个color关键字，您可能希望查找所有带有“color”：“blue”的文档，并且只在该子集上运行elastiknn_nearest_neighbors查询。为此，可以在function score query 或者是query rescorer中使用elastiknn_nearest_neighbors查询。

```json
GET /my-index/_search
{
  "size": 10,
  "query": {
    "function_score": {
      "query": {
        "term": { "color": "blue" }                  # 1
      },
      "functions": [                                 # 2
        {
          "elastiknn_nearest_neighbors": {           # 3
            "field": "vec",
            "similarity": "cosine",
            "model": "exact",
            "vec": {
              "values": [0.1, 0.2, 0.3, ...]
            }
          }
        }
      ]
    }
  }
}
```

这还不支持传递索引向量。

当使用“model”：“lsh”时，将忽略“candidates”参数，并且不会像使用elastiknn_nearest_neighbors查询一样，以精确的相似性对向量重新评分。相反，分数是：最大相似度分数*匹配哈希的比例。这是分数函数采用文档ID并且必须立即返回分数这一事实的必然结果。

```json
GET /my-index/_search
{
  "query": {
    "term": { "color": "blue" }                     # 1
  },
  "rescore": {
    "window_size": 10,                              # 2
    "query": {
      "rescore_query": {
        "elastiknn_nearest_neighbors": {            # 3
            "field" : "vec",
            "similarity" : "l2",
            "model" : "exact",            
            "vec" : {
                "values" : [0.1, 0.2, 0.3, ...]
            }
        }
      },
      "query_weight": 0,                            # 4
      "rescore_query_weight": 1                     # 5
    }
  }
}
```

