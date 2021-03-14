package com.lijingyu.thread07.pool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MyScheduleThreadPool {
    public static void main(String[] args) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(4);
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            scheduledExecutorService.scheduleAtFixedRate(() -> System.out.println(Thread.currentThread().getName() + "_" + finalI), 0, (long) (Math.random() * 6 + 1), TimeUnit.SECONDS);
        }
    }
}