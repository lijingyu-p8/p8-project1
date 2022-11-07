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

## 三、Callable的call方法

FutureTask实现RunnableFuture接口，通过run方法执行call()

```java
public void run() {
    // 保证任务的状态是NEW才可以运行
    // 基于CAS的方式，将当前线程设置为runner。
    if (state != NEW ||
        !UNSAFE.compareAndSwapObject(this, runnerOffset,
                                     null, Thread.currentThread()))
        return;
    try {
        Callable<V> c = callable;
        // 任务不为null，并且任务的状态还处于NEW
        if (c != null && state == NEW) {
            //存放返回结果
            V result;
            //判断任务是否正常结束
            boolean ran;
            try {
                result = c.call();
                ran = true;
            } catch (Throwable ex) {
                result = null;
                ran = false;
                //出现异常，封装异常信息
                setException(ex);
            }
            //正常执行结束，封装返回结果
            if (ran)
                set(result);
        }
    } finally {
        //将执行任务的runner设置为空
        runner = null;
        //如果状态是中断，则执行中断的处理逻辑
        int s = state;
        if (s >= INTERRUPTING)
            handlePossibleCancellationInterrupt(s);
    }
}
//设置返回结果
protected void set(V v) {
    //首先要将任务状态从NEW设置为COMPLETING
    if (UNSAFE.compareAndSwapInt(this, stateOffset, NEW, COMPLETING)) {
        //将返回结果设置给outcome。
        outcome = v;
        //将状态修改为NORMAL，代表正常结束
        UNSAFE.putOrderedInt(this, stateOffset, NORMAL);
        //完成下一个stage阶段
        finishCompletion();
    }
}
//设置异常
protected void setException(Throwable t) {
    if (UNSAFE.compareAndSwapInt(this, stateOffset, NEW, COMPLETING)) {
        //返回结果为异常信息
        outcome = t;
        UNSAFE.putOrderedInt(this, stateOffset, EXCEPTIONAL); // final state
        //完成下一个stage阶段
        finishCompletion();
    }
}
```

## 四、get方法获取返回结果

```java
public V get() throws InterruptedException, ExecutionException {
    //获取当前状态
    int s = state;
    //满足这个状态就代表现在可能还没有返回结果
    if (s <= COMPLETING) //set值的这一步if (UNSAFE.compareAndSwapInt(this, stateOffset, NEW, COMPLETING))
        //尝试挂起线程，等待拿结果
        s = awaitDone(false, 0L);
    //获取返回结果
    return report(s);
}
//线程要等待任务执行结束，等待任务执行的状态变为大于COMPLETING状态
private int awaitDone(boolean timed, long nanos) throws InterruptedException {
    //计算deadline，如果是get()，就是0，  如果是get(time,unit)那就追加当前系统时间
    final long deadline = timed ? System.nanoTime() + nanos : 0L;
    //构建WaitNode
    WaitNode q = null;
    //queued = false
    boolean queued = false;
    //死循环
    for (;;) {
        //这个get的线程是否中断了。
        if (Thread.interrupted()) {
            //将当前节点从waiters中移除。
            removeWaiter(q);
            //并且抛出中断异常
            throw new InterruptedException();
        }

        //拿到现在任务的状态
        int s = state;
        //判断任务是否已经执行结束了
        if (s > COMPLETING) {
            //如果设置过WaitNode，直接移除WaitNode的线程
            if (q != null)
                q.thread = null;
            //返回当前任务的状态
            return s;
        }
        //如果任务的状态处于 COMPLETING
        else if (s == COMPLETING)
            //COMPLETING的持续时间非常短，只需要做一手现成的让步即可。
            Thread.yield();
        //现在线程的状态是NEW，（call方法可能还没执行完呢，准备挂起线程）
        else if (q == null)
            //封装WaitNode存放当前线程
            q = new WaitNode();
        else if (!queued)
            //如果WaitNode还没有排在waiters中，现在就排进来（头插法的效果）
            queued = UNSAFE.compareAndSwapObject(this, waitersOffset, q.next = waiters, q);
        else if (timed) {
            //get(time,unit)挂起线程的方式
            //计算挂起时间
            nanos = deadline - System.nanoTime();
            //挂起的时间，是否小于等于0
            if (nanos <= 0L) {
                //移除waiters中的当前Node
                removeWaiter(q);
                //返回任务状态
                return state;
            }
            //正常指定挂起时间即可。（线程挂起）
            LockSupport.parkNanos(this, nanos);
        }
        else {
            //get()挂起线程的方式
            LockSupport.park(this);
        }
    }
}
//任务结束。
private V report(int s) throws ExecutionException {
    //拿到结果
    Object x = outcome;
    //判断是正常返回结束
    if (s == NORMAL)
        //返回结果
        return (V)x;
    //任务状态是大于取消
    if (s >= CANCELLED)
        //抛异常。
        throw new CancellationException();
    //抛异常。
    throw new ExecutionException((Throwable)x);
}

// 正常返回 report
// 异常返回 report
// 取消任务 report
// 中断任务 awaitDone
```

## 五、finishCompletion

```java
//任务状态已经变为了NORMAL，做一些后续处理
private void finishCompletion() {
    for (WaitNode q; (q = waiters) != null;) {
        //拿到第一个节点后，直接用CAS的方式，将其设置为null，waitersOffset代表等待node的指针
        if (UNSAFE.compareAndSwapObject(this, waitersOffset, q, null)) {
            for (;;) {
                //基于q拿到线程信息
                Thread t = q.thread;
                //线程不为null
                if (t != null) {
                    //WaitNode的thread设置为null
                    q.thread = null;
                    //唤醒这个线程
                    LockSupport.unpark(t);
                }
                //往后遍历，接着唤醒
                WaitNode next = q.next;
                if (next == null)
                    break;
                q.next = null;
                //指向next的WaitNode
                q = next;
            }
            break;
        }
    }

    //扩展方法，没任何实现，你可以自己实现
    done();

    // 任务处理完
    callable = null;   
}
```

