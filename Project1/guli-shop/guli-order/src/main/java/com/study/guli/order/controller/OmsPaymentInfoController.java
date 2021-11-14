package com.study.guli.order.controller;

import com.study.common.utils.PageUtils;
import com.study.common.utils.R;
import com.study.guli.order.entity.OmsPaymentInfoEntity;
import com.study.guli.order.service.OmsPaymentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;



/**
 * 支付信息表
 *
 * @author lijingyu
 * @email lijingyu@gmail.com
 * @date 2021-11-14 14:21:16
 */
@RestController
@RequestMapping("product/omspaymentinfo")
public class OmsPaymentInfoController {
    @Autowired
    private OmsPaymentInfoService omsPaymentInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = omsPaymentInfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		OmsPaymentInfoEntity omsPaymentInfo = omsPaymentInfoService.getById(id);

        return R.ok().put("omsPaymentInfo", omsPaymentInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody OmsPaymentInfoEntity omsPaymentInfo){
		omsPaymentInfoService.save(omsPaymentInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody OmsPaymentInfoEntity omsPaymentInfo){
		omsPaymentInfoService.updateById(omsPaymentInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		omsPaymentInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
