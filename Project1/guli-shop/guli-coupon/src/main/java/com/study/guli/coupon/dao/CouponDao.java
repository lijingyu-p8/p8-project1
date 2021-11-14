package com.study.guli.coupon.dao;

import com.study.guli.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author lijingyu
 * @email lijingyu@gmail.com
 * @date 2021-11-14 14:35:52
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
