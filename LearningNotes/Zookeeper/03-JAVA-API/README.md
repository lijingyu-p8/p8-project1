# ZooKeeper-Java-API

## 一、基本使用

```java
public class ZookeeperTest {
    private String connectString = "192.168.73.101:2181,192.168.73.102:2181,192.168.73.103:2181";
    private int sessionTimeout = 2000;
    private ZooKeeper zkClient = null;
    private CountDownLatch connectionLatch = new CountDownLatch(1);

    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ZookeeperTest zookeeperTest = new ZookeeperTest();
        try {
            zookeeperTest.connectZooKeeper();
            System.out.println("连接上");
            String path = "/" + System.currentTimeMillis();
            zookeeperTest.createNode(path);
            System.out.println("创建节点完毕");
            zookeeperTest.getChildren();
            System.out.println("获取子节点完毕");
            zookeeperTest.getData(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void connectZooKeeper() throws IOException, InterruptedException {
        zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                connectionLatch.countDown();
                // 收到事件通知后的回调函数（用户的业务逻辑）
                //zk的注册是一次性的。
                System.out.println(watchedEvent.getType() + "--" + watchedEvent.getPath());
            }
        });
        connectionLatch.await();
    }

    public void createNode(String path) throws InterruptedException, KeeperException {
        //acl:权限
        //CreateMode：节点模式，持久、临时、带序号
        String s = zkClient.create(path, "啦啦啦".getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("创建完毕：" + s);
    }

    public void getChildren() throws Exception {
        List<String> children = zkClient.getChildren("/", true);
        for (String child : children) {
            System.out.println("子目录：" + child);
        }
    }

    public void exist(String path) throws Exception {
        Stat stat = zkClient.exists(path, false);
        System.out.println(stat == null ? "not exist" : "exist");
    }

    public void getData(String path) throws InterruptedException, KeeperException {
        Stat stat = new Stat();
        byte[] data = zkClient.getData(path, false, stat);
        String s = new String(data);
        System.out.println("数据：" + s);
    }
}
```

