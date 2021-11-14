package com.study.guli.ware.dao;

import com.study.guli.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品库存
 * 
 * @author lijingyu
 * @email lijingyu@gmail.com
 * @date 2021-11-14 14:40:46
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {
	
}
