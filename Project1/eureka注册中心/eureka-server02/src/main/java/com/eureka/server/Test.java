package com.eureka.server;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Test {

//	public static void main(String[] args) {
//		Lock lock = new ReentrantLock();
//		Condition conditionT1 = lock.newCondition();
//		Condition conditionT2 = lock.newCondition();
//		CountDownLatch countDownLatch = new CountDownLatch(2);
//		long mainStart = System.currentTimeMillis();
//		new Thread(() -> {
//			try {
//				lock.lock();
//				char xiaoxie = 'a';
//				long start = System.currentTimeMillis();
//				for (int i = 0; i < 26; i++) {
//					System.out.print(xiaoxie);
//					xiaoxie++;
//					conditionT2.signal();
//					conditionT1.await();
//				}
//				conditionT2.signal();
//				long end = System.currentTimeMillis();
//				System.out.println();
//				System.out.println("小写字母打印时间ms：" + (end - start));
//				countDownLatch.countDown();
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				lock.unlock();
//			}
//		}, "t1").start();
//
//		new Thread(() -> {
//			try {
//				lock.lock();
//				char daxie = 'A';
//				long start = System.currentTimeMillis();
//				for (int i = 0; i < 26; i++) {
//					System.out.print(daxie);
//					daxie++;
//					conditionT1.signal();
//					conditionT2.await();
//				}
//				conditionT1.signal();
//				long end = System.currentTimeMillis();
//				System.out.println("大写字母打印时间ms：" + (end - start));
//				countDownLatch.countDown();
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				lock.unlock();
//			}
//		}, "t2").start();
//		try {
//			countDownLatch.await();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		long mainEnd = System.currentTimeMillis();
//		System.out.println("总耗时ms：" + (mainEnd - mainStart));
//	}
	public static void main(String[] args) {
		BigDecimal sum = new BigDecimal(0);
		for (int i = 2; i < 100; i++) {
			if (isZhiShu(i)) {
				BigDecimal jiecheng = getJieCheng(i);
				sum = sum.add(jiecheng);
			}
		}
		System.out.println("1-100以内质数阶乘之和为：" + sum);
	}

	private static BigDecimal getJieCheng(int num) {
		BigDecimal count = new BigDecimal(1);
		for (int i = 1; i <= num; i++) {
			count = count.multiply(new BigDecimal(i));
		}
		return count;
	}

	private static boolean isZhiShu(int num) {
		if (num == 2) {
			return true;
		}
		if (num % 2 == 0) {
			return false;
		}
		for (int i = 3; i < Math.sqrt(num); i++) {
			if (num % i == 0) {
				return false;
			}
		}
		return true;
	}
}