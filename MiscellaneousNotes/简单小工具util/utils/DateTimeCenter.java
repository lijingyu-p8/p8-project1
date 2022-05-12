package com.jiuqi.budget.common.utils;

import org.springframework.util.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 日期操作工具类
 * @author wangxing <br> 2019/6/15 23:21
 **/
public class DateTimeCenter {

	/**
	 * 最小时间 1970/1/1
	 */
	public static final LocalDate MIN_LOCAL_DATE = LocalDate.of(1970,1,1);

	/**
	 * 最小时间 00:00:00
	 */
	public static final LocalTime MIN_LOCAL_TIME = LocalTime.MIN;

	/**
	 * 最小时间 1970/1/1 00:00:00
	 **/
	public static final LocalDateTime MIN_LOCAL_DATE_TIME = LocalDateTime.of(MIN_LOCAL_DATE, MIN_LOCAL_TIME);

	/**
	 * 最大时间 23:59:59
	 */
	public static final LocalTime MAX_LOCAL_TIME = LocalTime.of(23,59,59);

	/**
	 * 最大日期 9999年12月31日
	 */
	public static final LocalDate MAX_LOCAL_DATE = LocalDate.of(9999, 12, 31);

	/**
	 * 最大时间 9999/12/31 23:59:59
	 **/
	public static final LocalDateTime MAX_LOCAL_DATE_TIME = LocalDateTime.of(MAX_LOCAL_DATE, MAX_LOCAL_TIME);

	/**
	 * 预算系统默认的时间格式化方式
	 **/
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	/**
	 * Date转换为LocalDateTime
	 * @param date 日期
	 * @return java.time.LocalDateTime
	 * @author wangxing <br> 2019/6/15 23:20
	 **/
	public static LocalDateTime convertDateToLDT(Date date) {
		if(date==null){
			return null;
		}
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}

	/**
	 * LocalDateTime转换为Date
	 * @param time 日期
	 * @return java.util.Date
	 * @author wangxing <br> 2019/6/15 23:20
	 **/
	public static Date convertLDTToDate(LocalDateTime time) {
		if(time==null){
			return null;
		}
		return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * 获取指定日期的毫秒
	 * @param time 指定日期
	 * @return java.lang.Long
	 * @author wangxing <br> 2019/6/15 23:21
	 **/
	public static Long convertLDTToLong(LocalDateTime time) {
		if(time == null){
			return null;
		}
		return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}

	/**
	 * 将long转换成LocalDateTime
	 * @param timestamp long类型的时间戳
	 */
	public static LocalDateTime convertLongToLDT(long timestamp) {
		Instant instant = Instant.ofEpochMilli(timestamp);
		ZoneId zone = ZoneId.systemDefault();
		return LocalDateTime.ofInstant(instant, zone);
	}

	/**
	 * 获取指定时间的指定格式
	 * @param time 指定日期
	 * @param pattern 具体格式
	 * @return java.lang.String
	 * @author wangxing <br> 2019/6/15 23:23
	 **/
	public static String formatTime(LocalDateTime time,String pattern) {
		if (time == null) {
			return null;
		}
		return time.format(DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 获取指定时间的指定格式
	 * @param time 指定日期
     * @return java.lang.String
     * @author wangxing <br> 2019/6/15 23:23
     **/
    public static String formatTime(LocalDateTime time) {
        if (time == null) {
            return null;
        }
        return time.format(DATE_TIME_FORMATTER);
    }

    /**
     * 获取指定时间的指定格式
     *
     * @param date 指定日期
     * @return java.lang.String
     **/
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(DATE_FORMATTER);
    }

    /**
     * 获取当前时间的指定格式
     *
     * @param pattern 指定格式
     * @return java.lang.String
     * @author wangxing <br> 2019/6/15 23:23
     **/
    public static String formatNow(String pattern) {
        return formatTime(LocalDateTime.now(), pattern);
    }
	
	/**
	 * 获取当前时间的默认格式
	 * @return java.lang.String
	 * @author wangxing <br> 2019/6/15 23:25
	 **/
	public static String formatNow(){
		return LocalDateTime.now().format(DATE_TIME_FORMATTER);
	}

	/**
	 * 将字符串转换为LocalDateTime
	 * @param time 时间字符串
	 * @return java.time.LocalDateTime
	 * @author wangxing <br> 2019/6/16 15:15
	 **/
	public static LocalDateTime parse2LocalDataTime(String time){
		if (StringUtils.isEmpty(time)) {
			return null;
		}
		return LocalDateTime.parse(time,DATE_TIME_FORMATTER);
	}

	/**
	 * 将字符串转换为LocalDate
	 * @param time 时间字符串
	 * @return java.time.LocalDateTime
	 * @author wangxing <br> 2019/6/16 15:15
	 **/
	public static LocalDate parse2LocalDate(String time){
		if (StringUtils.isEmpty(time)) {
			return null;
		}
		return LocalDate.parse(time,DATE_FORMATTER);
	}

	/**
	 * 将字符串转换为LocalDateTime
	 * @param time 时间字符串
	 * @param pattern 字符串格式
	 * @return java.time.LocalDateTime
	 * @author wangxing <br> 2019/6/16 15:16
	 **/
	public static LocalDateTime parse2LocalDataTime(String time, String pattern){
		if (StringUtils.isEmpty(time) || StringUtils.isEmpty(pattern)) {
			return null;
		}
		return LocalDateTime.parse(time,DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 现在
	 * @return java.time.LocalDateTime
	 * @author wangxing <br> 2019/6/16 10:40
	 **/
	public static LocalDateTime now(){
		return LocalDateTime.now();
	}

}
