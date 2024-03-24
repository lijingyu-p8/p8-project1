package com.minyu.knowledge.sea.jdk;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * 8--》17的新特性
 */
public class Java17New {
    public static void main(String[] args) throws FileNotFoundException {
        // 1、泛型
//        fanxing();
//        //2、try catch
//        tryCatch();
//        streamTest();
//        onlyReadList();
        futureTimeOut();
    }

    private static void futureTimeOut() {
        //返回默认值
        CompletableFuture<String> aDefault = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        }).completeOnTimeout("default", 1, TimeUnit.SECONDS);
        System.out.println(aDefault.join());
        //处理异常
        CompletableFuture<String> stringFuture = CompletableFuture.supplyAsync((Supplier<String>) () -> {
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return null;
                }).orTimeout(1, TimeUnit.SECONDS)
                .exceptionally(throwable -> {
                    throwable.printStackTrace();
                    return "myDefault";
                }).thenApply(s -> s + "apply");
        System.out.println(stringFuture.join());
    }

    private static void onlyReadList() {
        //普通版
        List<String> list = new ArrayList<>();
        list.add("Tom");
        list.add("Jerry");
        list.add("Mark");
        list.add("Jhon");
        list = Collections.unmodifiableList(list);
        System.out.println(list);
        //新版本只读集合1
        list = List.of("TOM", "Jerry", "Mark", "Ben");
        System.out.println(list);
        /**
         * 新版本只读集合2
         * copyOf方法的作用通过一个集合返回的是一个只读集合,如果参数本来就是只读集合,那么返回的就是参数,如果参数不是只读集合,就再创造一个只读集合返回
         */
        List<String> strings = List.copyOf(list);
    }

    /**
     * 新增transferTo方法
     *
     * @throws FileNotFoundException
     */
    private static void streamTest() throws FileNotFoundException {
        InputStream inputStream = new FileInputStream("d:/aaa.txt");
        OutputStream outputStream = new FileOutputStream("d:/bbb.txt");
        try (inputStream; outputStream) {
            inputStream.transferTo(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void tryCatch() throws FileNotFoundException {
        //普通版本
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(System.in);
            int read = reader.read();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // 这里可以释放资源
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // JAVA8 try catch finally语句块
        try (InputStreamReader isr = new InputStreamReader(new FileInputStream("d:/aaa.txt"));) {
            isr.read();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // JAVA9 try catch finally语句块
        InputStreamReader isr = new InputStreamReader(new FileInputStream("d:/aaa.txt"));
        OutputStreamWriter isw = new OutputStreamWriter(new FileOutputStream("d:/aaa.txt"));
        try (isr; isw) {
            isr.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void fanxing() {
        //可以不写明泛型类型
        Comparable<String> comparable = new Comparable<>() {
            @Override
            public int compareTo(String o) {
                return 0;
            }
        };
        //可以通过匿名内部类写法,在某个实例上完成对某个方法的重写
        Person<String> person = new Person<>() {
            @Override
            public void eat(String s) {
                super.eat(s);
            }
        };
    }

    /**
     * 测试Stream新增takeWhile方法
     * takeWhile  从流中的头开始取元素,直到不满足条件为止
     */
    public static void testTakeWhile() {
        List<Integer> list = Arrays.asList(1, 89, 63, 45, 72, 65, 41, 65, 82, 35, 95, 100);
        // 从头开始取所有奇数,直到遇见一个偶数为止
        list.stream().takeWhile(e -> e % 2 == 1).forEach(System.out::println);

    }

    /**
     * 测试Stream新增dropWhile方法
     * dropWhile  从头开始删除满足条件的数据,直到遇见第一个不满足的位置,并保留剩余元素
     */
    public static void testDropWhile() {
        List<Integer> list = Arrays.asList(2, 86, 63, 45, 72, 65, 41, 65, 82, 35, 95, 100);
        // 删除流开头所有的偶数,直到遇见奇数为止
        list.stream().dropWhile(e -> e % 2 == 0).forEach(System.out::println);

    }

    /**
     * 测试Stream新增ofNullable方法
     * ofNullable 允许创建Stream流时,只放入一个null
     */
    public static void testOfNullable() {
        // of方法获取流 ,允许元素中有多个null值
        Stream<Integer> stream1 = Stream.of(10, 20, 30, null);
        // 如果元素中只有一个null,是不允许的
        Stream<Integer> stream2 = Stream.of(null);
        // JAVA9中,如果元素为null,返回的是一个空Stream,如果不为null,返回一个只有一个元素的Stream
        Stream<Integer> stream3 = Stream.ofNullable(null);
    }

    /**
     * 测试Stream新增iterate方法
     * iterate指定种子数,指定条件和迭代方式来获取流
     */
    public static void testNewIterate() {
        //JAVA8通过 generate方法获取一个Stream
        Stream.generate(Math::random).limit(10).forEach(System.out::println);
        //JAVA8 通过iterate获取一个Stream
        Stream.iterate(0, t -> t + 2).limit(10).forEach(System.out::println);
        //JAVA9通过重载iterate获取Stream
        Stream.iterate(0, t -> t < 10, t -> t + 1).forEach(System.out::println);
    }
}
