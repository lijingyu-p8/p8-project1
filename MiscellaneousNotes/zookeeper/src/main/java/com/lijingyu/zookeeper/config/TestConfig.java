package com.lijingyu.zookeeper.config;

import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 测试类
 */
public class TestConfig {
    ZooKeeper zk;

    @Before
    public void conn() {
        zk = ZKUtils.getZK();
    }

    @After
    public void close() {
        try {
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getConf() {
        WatchCallBack watchCallBack = new WatchCallBack();
        watchCallBack.setZk(zk);
        MyConf myConf = new MyConf();
        watchCallBack.setConf(myConf);
        //等待，exits，获取到数据时，会释放锁
        watchCallBack.aWait();
        //1，节点不存在
        //2，节点存在
        while (true) {
            if (myConf.getConf().equals("")) {
                System.out.println("conf diu le ......");
                watchCallBack.aWait();
            } else {
                System.out.println(myConf.getConf());
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}