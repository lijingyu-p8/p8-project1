package com.lijingyu.zookeeper.config;

import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * 创建zk
 */
public class ZKUtils {

    private static ZooKeeper zk;

    private static String address = "192.168.150.11:2181,192.168.150.12:2181," +
            "192.168.150.13:2181,192.168.150.14:2181/testLock";

    private static DefaultWatch watch = new DefaultWatch();

    private static CountDownLatch init = new CountDownLatch(1);

    public static ZooKeeper getZK() {
        try {
            zk = new ZooKeeper(address, 1000, watch);
            //设置锁
            watch.setCc(init);
            //锁等待，连接上了zk，就会释放锁。
            init.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zk;
    }
}