package com.study.guli.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.study.common.utils.PageUtils;
import com.study.guli.product.entity.SkuInfoEntity;

import java.util.Map;

/**
 * sku信息
 *
 * @author lijingyu
 * @email lijingyu@gmail.com
 * @date 2021-11-14 14:26:11
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}
