# FutureTask源码解析

## 一、使用

在Java中可以通过实现runnable接口、继承thread类创建线程，也可以通过Callable、FutureTask实现带返回结果的线程。其中FutureTask中需要一个Callable作为入参，并且FutureTask实现Future接口，运行Callable并通过get方法拿到接口返回值。

```java
public class FutureTaskTest {

    public static void main(String[] args) throws Exception {
        FutureTask<String> futureTask = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("执行");
                return "执行完毕";
            }
        });
        Thread thread = new Thread(futureTask);
        thread.start();
        String s = futureTask.get();
        System.out.println(s);
    }
}
```

## 二、核心参数

```java
// NEW -> COMPLETING -> NORMAL        任务正常执行，返回结果是正常结果
// NEW -> COMPLETING -> EXCEPTIONAL   任务正常执行，返回结果为异常结果
// NEW -> CANCELLED                   任务异常结束，被取消了 
// NEW -> INTERRUPTING -> INTERRUPTED 任务被中断
//当前任务的运行状态
private volatile int state;
// 任务的初始化状态
private static final int NEW          = 0;
// 将Callable的结果（正常结果，异常结果）正在封装给当前的FutureTask的状态（时间很短）
private static final int COMPLETING   = 1;
//任务正常结束
private static final int NORMAL       = 2;
//执行任务时，发生了异常
private static final int EXCEPTIONAL  = 3;
//任务被取消了
private static final int CANCELLED    = 4;
//线程的中断状态，被设置为了true（现在还在运行）
private static final int INTERRUPTING = 5;
//线程被中断了
private static final int INTERRUPTED  = 6;
//当前要执行的任务
private Callable<V> callable;
//任务的返回结果或者发生异常时的异常信息
private Object outcome; 
//执行任务的线程
private volatile Thread runner;
//单向链表，存放通过get方法挂起等待的线程
private volatile WaitNode waiters;
```

