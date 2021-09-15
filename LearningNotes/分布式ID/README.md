# 分布式ID

## 雪花算法

```java
package com.example.demo.id;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneOffset;

public class IdWorker {
	/**
	 * 机器ID
	 */
	private long workerId;

	/**
	 * 数据中心ID
	 */
	private long datacenterId;

	/**
	 * 序列号
	 */
	private long sequence;

	/**
	 * 机器长度为5位
	 */
	private long workerIdBits = 5L;

	/**
	 * 数据中心id长度为5位
	 */
	private long datacenterIdBits = 5L;

	/**
	 * 机器id最大值 11111111 11111111 11111111 11111111 -1 11111111 11111111 11111111
	 * 11100000 左移5位 00000000 00000000 00000000 00011111 异或运算 最大值31
	 */
	private long maxWorkerId = -1L ^ (-1L << workerIdBits);

	/**
	 * 最大值31
	 */
	private long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

	/**
	 * 序列号id长度
	 */
	private long sequenceBits = 12L;

	/**
	 * 序列号最大值4095同一毫秒内，支持生成4095个id
	 */
	private long sequenceMask = -1L ^ (-1L << sequenceBits);

	/**
	 * 工作id需要左移的位数，12 位
	 */
	private long workerIdShift = sequenceBits;

	/**
	 * 数据id需要左移位数12+5=17 位
	 */
	private long datacenterIdShift = sequenceBits + workerIdBits;

	/**
	 * 水时间戳需要左移位数12+5+5=22位
	 */
	private long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

	/**
	 * 上次时间戳，初始值为负数
	 */
	private long lastTimestamp = -1L;

	private static IdWorker idWorker;
	static {
		idWorker = new IdWorker(10, 15, 1);
	}

	private IdWorker(long workerId, long datacenterId, long sequence) {
		// sanity check for workerId
		if (workerId > maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(
					String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
		}
		if (datacenterId > maxDatacenterId || datacenterId < 0) {
			throw new IllegalArgumentException(
					String.format(" datacenter Id can't be greater than%d or less than o", maxDatacenterId));
		}
		this.workerId = workerId;
		this.datacenterId = datacenterId;
		this.sequence = sequence;
	}

	public static IdWorker buildIdWorker() {
		return idWorker;
	}

	// 一个ID生成算法
	public synchronized long nextId() {
		long timestamp = timeGen();
		// 获取当前时间戳如果小于上次时间戳，则表示时间戳获取出现异常
		if (timestamp < lastTimestamp) {
			System.err.printf("clock is moving backwards. Rejecting requests until %d.", lastTimestamp);
			throw new RuntimeException(String.format(
					"Clock moved backwards. Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
		}
		// 获取当前时间戳如果等于上次时间戳（同一毫秒内），则在序列号加一;否则序列号赋值为0，从0开始。
		if (lastTimestamp == timestamp) {
			sequence = (sequence + 1) & sequenceMask;
			// 当序列号超过了最大值，与运算结果会为0，需要等待一毫秒，重新获取序列号
			if (sequence == 0) {
				timestamp = tilNextMillis(lastTimestamp);
			}
		} else {
			sequence = 0;
		}
		// 将上次时间戳值刷新
		lastTimestamp = timestamp;

		/**
		 * 返回结果: timestamp << timestampLeftShift表示将时间戳左移相应位数 
		 * (datacenterId <<datacenterIdShift)表示将数据id左移相应位数 
		 * (workerId < workerIdShift)表示将工作id左移相应位数
		 * │是按位或运算符，例如: x/ y，只有当x，y都为0的时候结果才为O，其它情况结果都为1。
		 * 因为各部分只有相应位上的值有意义，其它位上都是0，所以将各部分的值进行│运算就能得到最终拼接好的id
		 */
		return (timestamp << timestampLeftShift) | (datacenterId << datacenterIdShift) | (workerId << workerIdShift)
				| sequence;
	}

	// 获取时间戳，并与上次时间戳比较
	private long tilNextMillis(long lastTimestamp) {
		long timestamp = timeGen();
		// 等待一毫秒，获取新的时间戳
		while (timestamp <= lastTimestamp) {
			timestamp = timeGen();
		}
		return timestamp;
	}

	// 获取系统时间戳
	private long timeGen() {
		return System.currentTimeMillis();
	}

	public static void main(String[] args) {
		long currentTimeMillis = System.currentTimeMillis();
		System.out.println(currentTimeMillis);
		for (int i = 0; i < 10; i++) {
			long nextId = IdWorker.buildIdWorker().nextId();
			System.out.println(nextId);
		}
	}

	private static IdFormat parseId(long nextId) {
		String binary = Long.toBinaryString(nextId);
		if (binary.length() != 63) {
			return null;
		}
		IdFormat idFormat = new IdFormat();// 序列号
		String number = binary.substring(binary.length() - 12);
		int sequence = Integer.parseInt(number, 2);
		idFormat.setSequence(sequence);
		// workid
		number = binary.substring(binary.length() - 17, binary.length() - 12);
		int workid = Integer.parseInt(number, 2);
		idFormat.setWorkerId(workid);
		// datecenterid
		number = binary.substring(binary.length() - 22, binary.length() - 17);
		int datacenterId = Integer.parseInt(number, 2);
		idFormat.setDatacenterId(datacenterId);
		// time
		number = binary.substring(0, binary.length() - 22);
		long time = Long.parseLong(number, 2);
		idFormat.setTimestamp(time);
		LocalDateTime parse = LocalDateTime.ofEpochSecond(time / 1000, 0, ZoneOffset.ofHours(8));
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mnm:ss");
		String format = dateTimeFormatter.format(parse);
		idFormat.setTimeFormat(format);
		return idFormat;
	}
}

class IdFormat {
	private int sequence;
	private int workerId;
	private int datacenterId;
	private long timestamp;
	private String timeFormat;

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public int getWorkerId() {
		return workerId;
	}

	public void setWorkerId(int workerId) {
		this.workerId = workerId;
	}

	public int getDatacenterId() {
		return datacenterId;
	}

	public void setDatacenterId(int datacenterId) {
		this.datacenterId = datacenterId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getTimeFormat() {
		return timeFormat;
	}

	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}

	@Override
	public String toString() {
		return "IdFormat [sequence=" + sequence + ", workerId=" + workerId + ", datacenterId=" + datacenterId
				+ ", timestamp=" + timestamp + ", timeFormat=" + timeFormat + "]";
	}

}
```

