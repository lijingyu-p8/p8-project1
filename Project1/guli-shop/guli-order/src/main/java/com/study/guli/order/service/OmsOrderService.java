package com.study.guli.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.study.common.utils.PageUtils;
import com.study.guli.order.entity.OmsOrderEntity;

import java.util.Map;

/**
 * 订单
 *
 * @author lijingyu
 * @email lijingyu@gmail.com
 * @date 2021-11-14 14:21:15
 */
public interface OmsOrderService extends IService<OmsOrderEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

