package com.jiuqi.budget.common.utils;

import com.jiuqi.budget.common.domain.ResultVO;

/**
 * @author wangxing <br> 2019/7/9 09:43
 **/
public class ResultUtil {

    public static Integer SUCCESS_CODE = 1;
    public static Integer FAIL_CODE = 0;

    public static ResultVO<?> ok() {
        ResultVO<?> resultVO = new ResultVO<>();
        resultVO.setCode(SUCCESS_CODE);
        return resultVO;
    }

    /**
     * 成功新增
     *
     * @param obj 新增的对象
     * @return com.jiuqi.budget.common.domain.ResultVO
     * @author wangxing <br> 2019/7/3 14:22
     **/
    public static <T> ResultVO<T> okAdd(T obj) {
        ResultVO<T> resultVO = new ResultVO<>();
        resultVO.setData(obj);
        resultVO.setCode(SUCCESS_CODE);
        resultVO.setMsg("新增成功");
        return resultVO;
    }

    /**
     * 成功修改
     *
     * @param obj 修改的对象
     * @return com.jiuqi.budget.common.domain.ResultVO
     * @author wangxing <br> 2019/7/3 14:22
     **/
    public static <T> ResultVO<T> okUpdate(T obj) {
        ResultVO<T> resultVO = new ResultVO<>();
        resultVO.setData(obj);
        resultVO.setCode(SUCCESS_CODE);
        resultVO.setMsg("修改成功");
        return resultVO;
    }

    /**
     * 成功删除
     *
     * @param deleteCount 删除的条数
     * @return com.jiuqi.budget.common.domain.ResultVO
     * @author wangxing <br> 2019/7/3 14:22
     **/
    public static ResultVO<?> okDelete(int deleteCount) {
        ResultVO<?> resultVO = new ResultVO<>();
        resultVO.setCode(SUCCESS_CODE);
        resultVO.setMsg("成功删除" + deleteCount + "条");
        return resultVO;
    }

    /**
     * 成功
     * @param msg 系统消息
     * @return
     */
    public static ResultVO<?> ok(String msg) {
        ResultVO<?> resultVO = new ResultVO<>();
        resultVO.setMsg(msg);
        resultVO.setCode(SUCCESS_CODE);
        return resultVO;
    }

    /**
     * 成功
     * @param msg 系统消息
     * @param data 业务对象
     * @param <T>
     * @return
     */
    public static <T> ResultVO<T> ok(String msg, T data) {
        ResultVO<T> resultVO = new ResultVO<>();
        resultVO.setCode(SUCCESS_CODE);
        resultVO.setMsg(msg);
        resultVO.setData(data);
        return resultVO;
    }

    /**
     * 成功
     * @param data 业务对象
     * @param <T>
     * @return
     */
    public static <T> ResultVO<T> ok(T data) {
        ResultVO<T> resultVO = new ResultVO<>();
        resultVO.setCode(SUCCESS_CODE);
        resultVO.setData(data);
        return resultVO;
    }
}
