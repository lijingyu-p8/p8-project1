package com.lijingyu.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

public class App {
    public static void main(String[] args) throws Exception {
        //zk是有session概念的，没有连接池的概念
        //watch:观察，回调
        //watch的注册只发生在读类型调用，get，exites...等操作
        //第一类：new zk 时候，传入的watch，这个watch，session级别的，跟path 、node没有关系。
        //new zk传入的watch也是default的。当getdata（“”，watch:true）时，是调用的default的watch
        final CountDownLatch cd = new CountDownLatch(1);
        //连接断开之后，session保持3秒  sessionTimeout
        final ZooKeeper zk = new ZooKeeper("192.168.150.11:2181,192.168.150.12:2181,192.168.150.13:2181,192.168.150.14:2181",
                3000, new Watcher() {
            //Watch 的回调方法！
            @Override
            public void process(WatchedEvent event) {
                Event.KeeperState state = event.getState();
                Event.EventType type = event.getType();
                String path = event.getPath();
                System.out.println("new zk watch: " + event.toString());
                //zk状态
                switch (state) {
                    case Unknown:
                        break;
                    case Disconnected:
                        break;
                    case NoSyncConnected:
                        break;
                    case SyncConnected:
                        System.out.println("connected");
                        cd.countDown();
                        break;
                    case AuthFailed:
                        break;
                    case ConnectedReadOnly:
                        break;
                    case SaslAuthenticated:
                        break;
                    case Expired:
                        break;
                }
                //事件类型
                switch (type) {
                    case None:
                        break;
                    case NodeCreated:
                        break;
                    case NodeDeleted:
                        break;
                    case NodeDataChanged:
                        break;
                    case NodeChildrenChanged:
                        break;
                }
            }
        });
        //因为连接是异步的，所以此处用countdownlatch阻塞住主线程
        cd.await();
        ZooKeeper.States state = zk.getState();
        switch (state) {
            case CONNECTING:
                System.out.println("ing......");
                break;
            case ASSOCIATING:
                break;
            case CONNECTED:
                System.out.println("ed........");
                break;
            case CONNECTEDREADONLY:
                break;
            case CLOSED:
                break;
            case AUTH_FAILED:
                break;
            case NOT_CONNECTED:
                break;
        }
        //创建节点 acl 权限设置
        String pathName = zk.create("/ooxx", "olddata".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        final Stat stat = new Stat();
        byte[] node = zk.getData("/ooxx", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("getData watch: " + event.toString());
                try {
                    //因为watch是一次性的，所以每次watch，再注册下一个watch
                    zk.getData("/ooxx", this, stat);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, stat);

        System.out.println(new String(node));

        //触发回调
        Stat stat1 = zk.setData("/ooxx", "newdata".getBytes(), 0);
        //还会触发吗？--watch是一次性的
        Stat stat2 = zk.setData("/ooxx", "newdata01".getBytes(), stat1.getVersion());

        System.out.println("-------async start----------");
        //异步监听回调方法
        zk.getData("/ooxx", false, new AsyncCallback.DataCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
                System.out.println("-------async call back----------");
                System.out.println(ctx.toString());
                System.out.println(new String(data));
            }

        }, "abc");
        System.out.println("-------async over----------");
        Thread.sleep(2222222);
    }
}