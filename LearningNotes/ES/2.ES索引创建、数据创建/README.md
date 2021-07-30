# Elasticsearch

## 一、索引创建

7.0之后，索引的默认分片数为1，7.0之前默认为5。

```
PUT product(索引name)
{
  "settings": {
    
  },
  "mappings": {
    
  },
  "aliases": {
    "NAME": {}
  }
}
```



## 二、索引查询

- 查询所有

  查询ES中存在的所有的索引，类似于MySQL中的show table。

  ```
  GET _cat/indices?v  _cat表示查看的含义，indices表示索引的集合
  ```

  ![](images/索引查询1.png)

  ![](images/索引查询2.png)

- 查询单条

  ```
  GET product(product为索引name)
  ```

  ![](images/索引查询4.jpg)

  ![](images/索引查询3.jpg)

## 三、索引修改

## 四、索引删除

```
DELETE product(索引name)
```



## 五、创建数据

创建数据时，数据要为json格式。_doc代表type，默认为__doc，7.0中已经不提倡使用type，8.0之后会剔除。

当不指定id时，必须为post请求，ES会生成默认的id。当指定id（示例8888）时，也可以用put请求。

```
POST product/_doc
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

PUT product/_doc/8888
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

