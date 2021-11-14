package com.study.guli.order.dao;

import com.study.guli.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author lijingyu
 * @email lijingyu@gmail.com
 * @date 2021-11-14 14:31:08
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}