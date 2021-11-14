package com.study.guli.order.controller;

import com.study.common.utils.PageUtils;
import com.study.common.utils.R;
import com.study.guli.order.entity.OmsRefundInfoEntity;
import com.study.guli.order.service.OmsRefundInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;



/**
 * 退款信息
 *
 * @author lijingyu
 * @email lijingyu@gmail.com
 * @date 2021-11-14 14:21:16
 */
@RestController
@RequestMapping("product/omsrefundinfo")
public class OmsRefundInfoController {
    @Autowired
    private OmsRefundInfoService omsRefundInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = omsRefundInfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		OmsRefundInfoEntity omsRefundInfo = omsRefundInfoService.getById(id);

        return R.ok().put("omsRefundInfo", omsRefundInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody OmsRefundInfoEntity omsRefundInfo){
		omsRefundInfoService.save(omsRefundInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody OmsRefundInfoEntity omsRefundInfo){
		omsRefundInfoService.updateById(omsRefundInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		omsRefundInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
