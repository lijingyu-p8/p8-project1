package com.lijingyu.threadtest.thread06.future;

import java.util.concurrent.*;

public class My_Callable_Future {
    public static void main(String[] args) {
        Callable<String> callable = () -> "测试";
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<String> submit = executorService.submit(callable);
        try {
            System.out.println(submit.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }
}