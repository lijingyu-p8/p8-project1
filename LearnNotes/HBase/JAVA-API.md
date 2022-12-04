# JAVA-API

### 一、DDL操作

```java
package com.msb.mall.product.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * @author lijingyu
 */
public class HBaseDDLTest {
    public void conn() throws IOException {
        /**
         * 根据官方 API 介绍，HBase 的客户端连接由 ConnectionFactory 类来创建，用户使用完成
         * 之后需要手动关闭连接。同时连接是一个重量级的，推荐一个进程使用一个连接，对 HBase
         * 的命令通过连接中的两个属性 Admin 和 Table 来实现。
         */
        Configuration conf = HBaseConfiguration.create();
        //加载zookeeper的配置
        conf.set("hbase.zookeeper.quorum", "node02,node03,node04");
        Connection connection = ConnectionFactory.createConnection(conf);
        /**
         * 获取 admin
         *  admin 的连接是轻量级的 不是线程安全的 不推荐池化或者缓存这个连接
         */
        Admin admin = connection.getAdmin();
        //创建namespace
        createNameSpace(connection.getAdmin(), "namespace01")
                .close();
        //创建table
        createTable(connection.getAdmin(), "table01", "cf")
                .close();
        //连接使用完毕需要关闭
        admin.close();
        connection.close();
    }

    private Admin createTable(Admin admin, String str_tableName, String cfName) {
        TableName tableName = TableName.valueOf(str_tableName);
        try {
            if (!admin.tableExists(tableName)) {
                //定义表描述对象
                TableDescriptorBuilder tableDescriptorBuilder = TableDescriptorBuilder.newBuilder(tableName);
                //定义列族描述对象
                ColumnFamilyDescriptorBuilder columnFamilyDescriptorBuilder = ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(cfName));
                //添加列族信息给表
                tableDescriptorBuilder.setColumnFamily(columnFamilyDescriptorBuilder.build());
                if (admin.tableExists(tableName)) {
                    //禁用表
                    admin.disableTable(tableName);
                    admin.deleteTable(tableName);
                }
                admin.createTable(tableDescriptorBuilder.build());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return admin;
    }

    /**
     * 修改表格中一个列族的版本
     *
     * @param admin        admin
     * @param namespace    命名空间名称
     * @param tableName    表格名称
     * @param columnFamily 列族名称
     * @param version      版本
     */
    public static void modifyTable(Admin admin, String namespace,
                                   String tableName, String columnFamily,
                                   int version) {
        try {
            // 2. 调用方法修改表格
            // 2.0 获取之前的表格描述
            TableDescriptor descriptor = admin.getDescriptor(TableName.valueOf(namespace, tableName));
            // 2.1 创建一个表格描述建造者
            // 如果使用填写 tableName 的方法 相当于创建了一个新的表格描述建造者 没有之前的信息
            // 如果想要修改之前的信息 必须调用方法填写一个旧的表格描述
            TableDescriptorBuilder tableDescriptorBuilder = TableDescriptorBuilder.newBuilder(descriptor);
            // 2.2 对应建造者进行表格数据的修改
            ColumnFamilyDescriptor columnFamily1 =
                    descriptor.getColumnFamily(Bytes.toBytes(columnFamily));
            // 创建列族描述建造者
            // 需要填写旧的列族描述
            ColumnFamilyDescriptorBuilder
                    columnFamilyDescriptorBuilder =
                    ColumnFamilyDescriptorBuilder.newBuilder(columnFamily1);
            // 修改对应的版本
            columnFamilyDescriptorBuilder.setMaxVersions(version);
            // 此处修改的时候 如果填写的新创建 那么别的参数会初始化
            tableDescriptorBuilder.modifyColumnFamily(columnFamilyDescriptorBuilder.build());
            admin.modifyTable(tableDescriptorBuilder.build());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除表格
     *
     * @param admin     admin
     * @param namespace 命名空间名称
     * @param tableName 表格名称
     * @return true 表示删除成功
     */
    public static boolean deleteTable(Admin admin, String namespace, String tableName) {
        // 调用相关的方法删除表格
        try {
            // HBase 删除表格之前 一定要先标记表格为不可以
            TableName tableName1 = TableName.valueOf(namespace, tableName);
            admin.disableTable(tableName1);
            admin.deleteTable(tableName1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private Admin createNameSpace(Admin admin, String namespace) {
        NamespaceDescriptor.Builder builder = NamespaceDescriptor.create(namespace)
                //给命令空间添加需求
                .addConfiguration("user", "admin");
        NamespaceDescriptor namespaceDescriptor = builder.build();
        try {
            admin.createNamespace(namespaceDescriptor);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return admin;
    }
}
```

### 二、数据操作

```java
package com.msb.mall.product.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.ColumnValueFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * @author lijingyu
 */
public class HBaseDataOpTest {

    public void conn() throws IOException {
        /**
         * 根据官方 API 介绍，HBase 的客户端连接由 ConnectionFactory 类来创建，用户使用完成
         * 之后需要手动关闭连接。同时连接是一个重量级的，推荐一个进程使用一个连接，对 HBase
         * 的命令通过连接中的两个属性 Admin 和 Table 来实现。
         */
        Configuration conf = HBaseConfiguration.create();
        //加载zookeeper的配置
        conf.set("hbase.zookeeper.quorum", "node02,node03,node04");
        Connection connection = ConnectionFactory.createConnection(conf);
        //插入数据
        putCellData(connection, "namespace01", "table01", "rowKey01",
                "cf01", "col-01", "value01");
        getCells(connection, "namespace01", "table01",
                "cf01", "rowKey01", "colName01");
    }

    /**
     * 插入数据
     *
     * @param connection
     * @param namespace    命名空间名称
     * @param tableName    表格名称
     * @param rowKey       主键
     * @param columnFamily 列族名称
     * @param columnName   列名
     * @param value        值
     */
    public static void putCellData(Connection connection, String namespace, String tableName,
                                   String rowKey, String columnFamily, String columnName,
                                   String value) throws IOException {
        // 1. 获取 table
        Table table = connection.getTable(TableName.valueOf(namespace, tableName));
        // 2. 调用相关方法插入数据
        // 2.1 创建 put 对象
        Put put = new Put(Bytes.toBytes(rowKey));
        // 2.2. 给 put 对象添加数据
        put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(columnName), Bytes.toBytes(value));
        // 2.3 将对象写入对应的方法
        try {
            table.put(put);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 3. 关闭 table
        table.close();
    }

    /**
     * 读取数据 读取对应的一行中的某一列
     *
     * @param connection
     * @param namespace    命名空间名称
     * @param tableName    表格名称
     * @param columnFamily 列族名称
     * @param rowKey       主键
     * @param columnName   列名
     */
    public static void getCells(Connection connection, String namespace, String tableName,
                                String columnFamily, String rowKey, String columnName) throws
            IOException {
        // 1. 获取 table
        Table table = connection.getTable(TableName.valueOf(namespace, tableName));
        // 2. 创建 get 对象
        Get get = new Get(Bytes.toBytes(rowKey));
        try {
            /**
             * 可以添加过滤器
             */
            FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ONE);
            SingleColumnValueFilter singleColumnValueFilter = new SingleColumnValueFilter(
                    // 列族名称
                    Bytes.toBytes(columnFamily),
                    // 列名
                    Bytes.toBytes(columnName),
                    // 比较关系
                    CompareOperator.EQUAL,
                    // 值
                    Bytes.toBytes("value")
            );
            filterList.addFilter(singleColumnValueFilter);
            get.setFilter(filterList);
            /**
             * 如果直接调用 get 方法读取数据 此时读一整行数据
             * 如果想读取某一列的数据 需要添加对应的参数
             */
            get.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(columnName));
            // 读取数据 得到 result 对象
            Result result = table.get(get);
            Cell cell1 = result.getColumnLatestCell(Bytes.toBytes(columnFamily), Bytes.toBytes("name"));
            Cell cell2 = result.getColumnLatestCell(Bytes.toBytes(columnFamily), Bytes.toBytes("age"));
            Cell cell3 = result.getColumnLatestCell(Bytes.toBytes(columnFamily), Bytes.toBytes("sex"));
            String name = Bytes.toString(CellUtil.cloneValue(cell1));
            String age = Bytes.toString(CellUtil.cloneValue(cell2));
            String sex = Bytes.toString(CellUtil.cloneValue(cell3));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 关闭 table
        table.close();
    }

    /**
     * 扫描数据
     *
     * @param connection connection
     * @param namespace  命名空间
     * @param tableName  表格名称
     * @param startRow   开始的 row 包含的
     * @param stopRow    结束的 row 不包含
     */
    public static void scanRows(Connection connection, String namespace, String tableName,
                                String startRow, String stopRow) throws IOException {
        // 1. 获取 table
        Table table = connection.getTable(TableName.valueOf(namespace, tableName));
        // 2. 创建 scan 对象
        Scan scan = new Scan();
        // 如果此时直接调用 会直接扫描整张表
        // 添加参数 来控制扫描的数据
        // 默认包含
        scan.withStartRow(Bytes.toBytes(startRow));
        // 默认不包含
        scan.withStopRow(Bytes.toBytes(stopRow));
        try {
            // 读取多行数据 获得 scanner
            ResultScanner scanner = table.getScanner(scan);
            // result 来记录一行数据 cell 数组
            // ResultScanner 来记录多行数据 result 的数组
            for (Result result : scanner) {
                //获取指定列族下指定列最新数据
                Cell columnLatestCell = result.getColumnLatestCell(Bytes.toBytes("cf01"), Bytes.toBytes("colName01"));
                Cell[] cells = result.rawCells();
                for (Cell cell : cells) {
                    System.out.print(new String(CellUtil.cloneRow(cell)) + "-" +
                            new String(CellUtil.cloneFamily(cell)) + "-" +
                            new String(CellUtil.cloneQualifier(cell)) + "-" +
                            new String(CellUtil.cloneValue(cell)) + "\t");
                }
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 3. 关闭 table
        table.close();
    }

    /**
     * 带过滤的扫描
     *
     * @param connection   connection
     * @param namespace    命名空间
     * @param tableName    表格名称
     * @param startRow     开始 row
     * @param stopRow      结束 row
     * @param columnFamily 列族名称
     * @param columnName   列名
     * @param value        value 值
     * @throws IOException
     */
    public static void filterScan(Connection connection, String namespace, String tableName,
                                  String startRow, String stopRow, String columnFamily,
                                  String columnName, String value) throws IOException {
        // 1. 获取 table
        Table table = connection.getTable(TableName.valueOf(namespace, tableName));
        // 2. 创建 scan 对象
        Scan scan = new Scan();
        // 如果此时直接调用 会直接扫描整张表
        // 添加参数 来控制扫描的数据
        // 默认包含
        scan.withStartRow(Bytes.toBytes(startRow));
        // 默认不包含
        scan.withStopRow(Bytes.toBytes(stopRow));
        // 可以添加多个过滤
        /**
         * 所有过滤条件必须全部满足（默认）
         */
//        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
        /**
         * 所有过滤条件中只需要满足一个即可
         */
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ONE);
        // 创建过滤器
        // (1) 结果只保留当前列的数据
        ColumnValueFilter columnValueFilter = new ColumnValueFilter(
                // 列族名称
                Bytes.toBytes(columnFamily),
                // 列名
                Bytes.toBytes(columnName),
                // 比较关系
                CompareOperator.EQUAL,
                // 值
                Bytes.toBytes(value)
        );
        // (2) 结果保留整行数据
        // 结果同时会保留没有当前列的数据
        SingleColumnValueFilter singleColumnValueFilter = new SingleColumnValueFilter(
                // 列族名称
                Bytes.toBytes(columnFamily),
                // 列名
                Bytes.toBytes(columnName),
                // 比较关系
                CompareOperator.EQUAL,
                // 值
                Bytes.toBytes(value)
        );

        // 本身可以添加多个过滤器
        filterList.addFilter(singleColumnValueFilter);
        filterList.addFilter(columnValueFilter);
        // 添加过滤
        scan.setFilter(filterList);
        try {
            // 读取多行数据 获得 scanner
            ResultScanner scanner = table.getScanner(scan);
            // result 来记录一行数据 cell 数组
            // ResultScanner 来记录多行数据 result 的数组
            for (Result result : scanner) {
                Cell[] cells = result.rawCells();
                for (Cell cell : cells) {
                    System.out.print(new
                            String(CellUtil.cloneRow(cell)) + "-" + new
                            String(CellUtil.cloneFamily(cell)) + "-" + new
                            String(CellUtil.cloneQualifier(cell)) + "-" + new
                            String(CellUtil.cloneValue(cell)) + "\t");
                }
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 3. 关闭 table
        table.close();
    }

    /**
     * 删除 column 数据
     *
     * @param connection
     * @param nameSpace
     * @param tableName
     * @param rowKey
     * @param family
     * @param column
     * @throws IOException
     */
    public static void deleteColumn(Connection connection, String nameSpace, String tableName,
                                    String rowKey, String family, String column) throws IOException {
        // 1.获取 table
        Table table = connection.getTable(TableName.valueOf(nameSpace, tableName));
        // 2.创建 Delete 对象
        Delete delete = new Delete(Bytes.toBytes(rowKey));
        // 3.添加删除信息
        // 3.1 删除单个版本
        delete.addColumn(Bytes.toBytes(family), Bytes.toBytes(column));
        // 3.2 删除所有版本
        delete.addColumns(Bytes.toBytes(family), Bytes.toBytes(column));
        // 3.3 删除列族
        // delete.addFamily(Bytes.toBytes(family));
        // 3.删除数据
        table.delete(delete);
        // 5.关闭资源
        table.close();
    }

}
```

