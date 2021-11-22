package com.lijingyu.juc.completablefuture;

import java.util.concurrent.*;

public class CompletableFutureDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        m6();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void m6() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "\t" + "---come in ");
            return 10;
        });

        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "\t" + "---come in ");
            return 20;
        });

        CompletableFuture<Integer> thenCombineResult = completableFuture1.thenCombine(completableFuture2, (x, y) -> {
            System.out.println(Thread.currentThread().getName() + "\t" + "---come in ");
            return x + y;
        });

        System.out.println(thenCombineResult.get());
    }

    private static void m5() throws ExecutionException, InterruptedException {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 20, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(50), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 11;
        }, threadPoolExecutor);
        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 22;
        }, threadPoolExecutor);
        CompletableFuture<Object> objectCompletableFuture = completableFuture1.applyToEither(completableFuture2, integer -> {
            System.out.println(integer);
            return integer;
        });
        threadPoolExecutor.shutdown();
    }

    private static void m4() throws ExecutionException, InterruptedException {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 20, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(50), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 11;
        }, threadPoolExecutor).thenAccept(r -> {
            System.out.println("结果" + r);
        });
        threadPoolExecutor.shutdown();
    }

    private static void m3() throws ExecutionException, InterruptedException {
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
                    System.out.println(10 / 0);
                    System.out.println(Thread.currentThread().getName() + "----" + r);
                    return r + 10;
                }).handle((r, throwable) -> {
                    System.out.println("yichang");
                    return r + 11;
                }).whenComplete((r, e) -> {
                    System.out.println(r);
                }).
                exceptionally(throwable -> {
                    throwable.printStackTrace();
                    return 200;
                });
        Integer integer = supplyAsync.get();
        System.out.println(Thread.currentThread().getName() + "----" + integer);
        threadPoolExecutor.shutdown();
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
        }, threadPoolExecutor).thenApplyAsync(r -> {
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

    private static void m1() throws ExecutionException, InterruptedException {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 20, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(50), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            System.out.println("111");
        }, threadPoolExecutor);
    }

}