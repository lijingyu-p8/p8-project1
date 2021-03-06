package com.lijingyu.thread06.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class MyCompletableFuture {
    public static void main(String[] args) {
        CompletableFuture<String> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(1);
            return "1";
        });
        CompletableFuture<String> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(2);
            return "2";
        });
        CompletableFuture<String> completableFuture3 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(3);
            return "3";
        });
        CompletableFuture.allOf(completableFuture1, completableFuture2, completableFuture3).join();
        try {
            System.out.println("第一个结果" + completableFuture1.get());
            System.out.println("第二个结果" + completableFuture2.get());
            System.out.println("第三个结果" + completableFuture3.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}