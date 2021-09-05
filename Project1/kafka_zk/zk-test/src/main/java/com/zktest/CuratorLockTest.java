package com.zktest;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.TimeUnit;

public class CuratorLockTest {
    private String rootNode = "/locks";
    // zookeeper server 列表
    private String connectString = "192.168.73.101:2181,192.168.73.102:2181,192.168.73.103:2181";
    // connection 超时时间
    private int connectionTimeout = 15000;
    // session 超时时间
    private int sessionTimeout = 60000;

    public static void main(String[] args) {
        new CuratorLockTest().test();
    }

    // 测试
    private void test() {
        // 创建分布式锁1
        InterProcessLock lock1 = new InterProcessMutex(getCuratorFramework(), rootNode);
        // 创建分布式锁2
        InterProcessLock lock2 = new InterProcessMutex(getCuratorFramework(), rootNode);
        new Thread(() -> {
            // 获取锁对象
            try {
//                lock1.acquire();
                lock1.acquire(2000, TimeUnit.SECONDS);
                System.out.println("线程1 获取锁");
                // 测试锁重入
                lock1.acquire();
                System.out.println("线程1 再次获取锁");
                Thread.sleep(5 * 1000);
                lock1.release();
                System.out.println("线程1 释放锁");
                lock1.release();
                System.out.println("线程1 再次释放锁");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            // 获取锁对象
            try {
                lock2.acquire();
                System.out.println("线程2 获取锁");
                // 测试锁重入
                lock2.acquire();
                System.out.println("线程2 再次获取锁");
                Thread.sleep(5 * 1000);
                lock2.release();
                System.out.println("线程2 释放锁");
                lock2.release();
                System.out.println("线程2 再次释放锁");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    // 分布式锁初始化
    public CuratorFramework getCuratorFramework() {
        //重试策略，初试时间3 秒，重试3 次
        RetryPolicy policy = new ExponentialBackoffRetry(1000, 3);
        //通过工厂创建Curator
        CuratorFramework client =
                CuratorFrameworkFactory.builder()
                        .connectString(connectString)
                        .connectionTimeoutMs(connectionTimeout)
                        .sessionTimeoutMs(sessionTimeout)
                        .retryPolicy(policy).build();
        //开启连接
        client.start();
        System.out.println("zookeeper 初始化完成...");
        return client;
    }
}