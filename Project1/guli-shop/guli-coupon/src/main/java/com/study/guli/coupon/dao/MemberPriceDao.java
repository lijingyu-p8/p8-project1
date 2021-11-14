package com.study.guli.coupon.dao;

import com.study.guli.coupon.entity.MemberPriceEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品会员价格
 * 
 * @author lijingyu
 * @email lijingyu@gmail.com
 * @date 2021-11-14 14:35:53
 */
@Mapper
public interface MemberPriceDao extends BaseMapper<MemberPriceEntity> {
	
}