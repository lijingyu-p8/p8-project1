# JVM常用参数

```properties
-XX：+PrintFlagsInitial：查看所有的参数的默认初始值
-XX：+PrintFlagsFinal：查看所有的参数的最终值（可能会存在修改，不再是初始值）
-Xms：初始堆空间内存（默认为物理内存的1/64）
-Xmx：最大堆空间内存（默认为物理内存的1/4）
-Xmn：设置新生代的大小。（初始值及最大值）
-XX:NewRatio：配置新生代与老年代在堆结构的占比
-XX:SurvivorRatio：设置新生代中Eden和S0/S1空间的比例
-XX:MaxTenuringThreshold：设置新生代垃圾的最大年龄
-XX：+PrintGCDetails：输出详细的GC处理日志
打印gc简要信息：①-Xx：+PrintGC ② - verbose:gc
-XX:HandlePromotionFalilure：是否设置空间分配担保
-XX:+UseTLAB 使用TLAB，默认打开
-XX:+PrintTLAB 打印TLAB的使用情况
-XX:TLABSize 设置TLAB大小
-XX:+DisableExplictGC System.gc()不管用 ，FGC
-Xloggc:opt/log/gc-%t.log //打印GC日志
-XX:+UseGCLogFileRotation --循环     
-XX:MaxTenuringThreshold GC升代年龄，最大值15
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=<filename.hprof> //目录不会自己创建
```

## Parallel Scavenge 回收器参数设置

- -XX:+UseParallelGC：手动指定年轻代使用Parallel并行收集器执行内存回收任务，自动会匹配PO收集老年代。

- -XX:+UseParallelOldGC：手动指定老年代使用并行回收收集器。

  上面两个参数分别适用于新生代和老年代。默认jdk8是开启的。默认开启一个，另一个也会被开启。（互相激活）

- -XX:ParallelGCThreads：设置年轻代并行收集器的线程数。一般地，最好与CPU核数相等，以避免过多的线程数影响垃圾收集性能。

  1. 在默认情况下，当CPU核数小于8个，ParallelGCThreads的值等于CPU核数。
  2. 当CPU核数大于8个，ParallelGCThreads的值等于3+[5*CPU_Count]/8]

- -XX:MaxGCPauseMillis：设置垃圾收集器最大停顿时间（即STW的时间）。单位是毫秒。

  1. 为了尽可能地把停顿时间控制在-XX:MaxGCPauseMillis 以内，收集器在工作时会调整Java堆大小或者其他一些参数。
  2. 对于用户来讲，停顿时间越短体验越好。但是在服务器端，我们注重高并发，整体的吞吐量。所以服务器端适合Parallel，进行控制。
  3. 该参数使用需谨慎。

- -XX:GCTimeRatio：垃圾收集时间占总时间的比例，即等于 1 / (N+1) ，用于衡量吞吐量的大小。

  1. 取值范围(0, 100)。默认值99，也就是垃圾回收时间占比不超过1。
  2. 与前一个-XX:MaxGCPauseMillis参数有一定矛盾性，STW暂停时间越长，Radio参数就容易超过设定的比例。

- -XX:+UseAdaptiveSizePolicy：设置Parallel Scavenge收集器具有自适应调节策略

  1. 在这种模式下，年轻代的大小、Eden和Survivor的比例、晋升老年代的对象年龄等参数会被自动调整，已达到在堆大小、吞吐量和停顿时间之间的平衡点。
  2. 在手动调优比较困难的场合，可以直接使用这种自适应的方式，仅指定虚拟机的最大堆、目标的吞吐量（GCTimeRatio）和停顿时间（MaxGCPauseMillis），让虚拟机自己完成调优工作。

## CMS回收器参数设置

- -XX:+UseConcMarkSweepGC：手动指定使用CMS收集器执行内存回收任务。

  开启该参数后会自动将-XX:+UseParNewGC打开。即：ParNew（Young区）+CMS（Old区）+Serial Old（Old区备选方案）的组合。

- -XX:CMSInitiatingOccupanyFraction：设置堆内存使用率的阈值，一旦达到该阈值，便开始进行回收。

  1. JDK5及以前版本的默认值为68，即当老年代的空间使用率达到68%时，会执行一次CMS回收。JDK6及以上版本默认值为92%
  2. 如果内存增长缓慢，则可以设置一个稍大的值，大的阀值可以有效降低CMS的触发频率，减少老年代回收的次数可以较为明显地改善应用程序性能。反之，如果应用程序内存使用率增长很快，则应该降低这个阈值，以避免频繁触发老年代串行收集器。因此通过该选项便可以有效降低Full GC的执行次数。

- -XX:+UseCMSCompactAtFullCollection：用于指定在执行完Full GC后对内存空间进行压缩整理，以此避免内存碎片的产生。不过由于内存压缩整理过程无法并发执行，所带来的问题就是停顿时间变得更长了。

- -XX:CMSFullGCsBeforeCompaction：设置在执行多少次Full GC后对内存空间进行压缩整理。

- -XX:ParallelCMSThreads：设置CMS的线程数量。

## G1回收器参数设置

- -XX:+UseG1GC：手动指定使用G1垃圾收集器执行内存回收任务
- -XX:G1HeapRegionSize：设置每个Region的大小。值是2的幂，范围是1MB到32MB之间，目标是根据最小的Java堆大小划分出约2048个区域。默认是堆内存的1/2000。
- -XX:MaxGCPauseMillis：设置期望达到的最大GC停顿时间指标，JVM会尽力实现，但不保证达到。默认值是200ms
- -XX:+ParallelGCThread：设置STW工作线程数的值。最多设置为8
- -XX:ConcGCThreads：设置并发标记的线程数。将n设置为并行垃圾回收线程数（ParallelGcThreads）的1/4左右。
- -XX:InitiatingHeapOccupancyPercent：设置触发并发GC周期的Java堆占用率阈值。超过此值，就触发GC。默认值是45。堆空间已用占比达到45%，老年代才会并发标记。



