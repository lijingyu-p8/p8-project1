package com.minyu.knowledge.sea.juc;

import java.util.concurrent.*;

/**
 * @Description: TODO
 * @Author: lijingyu
 * @CreateTime: 2023-02-20  22:19
 */
public class FutureTaskDemo {
    public static void main(String[] args) {
        FutureTask<Integer> futureTask = new FutureTask<>(() -> {
            return 1;
        });
        try {
            new Thread(futureTask).start();
            //get方法阻塞。
            Integer integer = futureTask.get();
            System.out.println(integer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static void m2() throws ExecutionException, InterruptedException {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 20, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(50), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        CompletableFuture<Integer> supplyAsync = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 11;
        }, threadPoolExecutor).thenApply(r -> {
//            System.out.println(10/0);
            System.out.println(Thread.currentThread().getName() + "----" + r);
            return r + 10;
        }).whenComplete((r, throwable) -> {
            System.out.println(Thread.currentThread().getName() + "----" + r);
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return 20;
        });
        Integer integer = supplyAsync.get();
        System.out.println(Thread.currentThread().getName() + "----" + integer);
        threadPoolExecutor.shutdown();
    }
}
