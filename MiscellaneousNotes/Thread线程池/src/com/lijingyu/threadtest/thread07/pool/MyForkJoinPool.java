package com.lijingyu.threadtest.thread07.pool;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class MyForkJoinPool {
    public static void main(String[] args) {
        List<Student> list = new ArrayList<>(100_0000);
        for (int i = 0; i < 100_0000; i++) {
            list.add(new Student("张三", new Random().nextInt(10000)));
        }
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        MyRecursiveTask task = new MyRecursiveTask(0, list.size(), list);
        forkJoinPool.execute(task);
        Set<Integer> ages = task.join();
        System.out.println(ages.size());
        System.out.println(ages);
        System.out.println(ages.size());
    }
}

class MyRecursiveTask extends RecursiveTask<Set<Integer>> {
    private int start;
    private int end;
    private List<Student> list;
    private int num = 1000;

    public MyRecursiveTask(int start, int end, List<Student> list) {
        this.start = start;
        this.end = end;
        this.list = list;
    }

    @Override
    protected Set<Integer> compute() {
        if ((end - start) < num) {
            Set<Integer> ages = new HashSet<>();
            for (int i = start; i < end; i++) {
                ages.add(list.get(i).getAge());
            }
            return ages;
        } else {
            int middle = start + (end - start) / 2;
            MyRecursiveTask subTask1 = new MyRecursiveTask(start, middle, list);
            MyRecursiveTask subTask2 = new MyRecursiveTask(middle, end, list);
            subTask1.fork();
            subTask2.fork();
            Set<Integer> join = subTask1.join();
            join.addAll(subTask2.join());
            return join;
        }
    }
}

class MyRecursiveAction extends RecursiveAction {

    @Override
    protected void compute() {
        //无返回值的
    }
}

class Student {
    private String name;
    private int age;

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}