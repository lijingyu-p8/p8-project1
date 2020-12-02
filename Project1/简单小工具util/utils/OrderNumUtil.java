package com.jiuqi.budget.common.utils;

import com.jiuqi.va.mapper.common.ApplicationContextRegister;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 序列号工具类
 *
 * @author wangxing
 */
public class OrderNumUtil {

    /**
     * 根据系统毫秒时间生成排序字段 (多台物理机要以其中某一台物理机为标准做时间同步）
     * <br>  按毫秒内100000条记录的极限可能，公元2286年前流水号总长度不会超过19位，固定6位小数。
     *
     * @return BigDecimal
     */
    public static BigDecimal getOrderNumByCurrentTimeMillis() {
        long currentTime = System.currentTimeMillis();
        long orderNum = getOrderNum(String.valueOf(currentTime), TimeUnit.MILLISECONDS);
        return new BigDecimal(currentTime + orderNum / 1000000.0);
    }

    /**
     *  利用Redis获得分布式系统下自增流水号（多台物理机要以其中某一台物理机为标准做时间同步）
     *
     * @param key  关键字
     * <br> 关键字: 考虑到Redis可靠性因素，不建议使用常量值做为key去获得从0到无限的流水号。
     * <br> 关键字: 建议key结合天、小时、分、秒、毫秒等时间单位为关键值， 不建议比天还大的时间单位。
     *
     * @param expireTimeUnit 过期时间单位，配合Key中的时间单位做过期设置。
     * @return
     */
    public static long getOrderNum(String key, TimeUnit expireTimeUnit) {
        StringRedisTemplate redisTemplate = ApplicationContextRegister.getBean(StringRedisTemplate.class);
        RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, Objects.requireNonNull(redisTemplate.getConnectionFactory()));
        long increment = entityIdCounter.getAndIncrement();
        // 过期时间
        if (increment == 0) {
            entityIdCounter.expire(1, expireTimeUnit);
        }
        return increment;
    }

}
