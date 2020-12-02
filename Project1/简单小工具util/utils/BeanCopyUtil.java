package com.jiuqi.budget.common.utils;

import com.jiuqi.budget.common.exception.BudgetException;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;

import java.util.ArrayList;
import java.util.List;

/**
 * 对象属性拷贝工具类
 * @author wangxing
 * @date 2020/5/6 11:38
 */
public class BeanCopyUtil {

    /**
     * 对象属性复制，<b style="color: red">这是一个浅拷贝</b>
     * @param srcObj 源对象
     * @param tarClazz 目标类型
     * @param <T> 目标泛型
     * @return 目标对象
     */
    public static <T> T copyObj(Object srcObj, Class<T> tarClazz){
        BeanCopier copier = BeanCopier.create(srcObj.getClass(), tarClazz, false);
        try {
            T tarObj = tarClazz.newInstance();
            copier.copy(srcObj,tarObj,null);
            return tarObj;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new BudgetException("对象属性拷贝出错！");
    }

    /**
     * 对象属性复制，<b style="color: red">这是一个浅拷贝</b>
     * @param srcObj 源对象
     * @param tarObj 目标对象
     * @param <T> 目标泛型
     * @return 目标对象
     */
    public static <T> T copyObj(Object srcObj, T tarObj){
        BeanCopier copier = BeanCopier.create(srcObj.getClass(), tarObj.getClass(), false);
        copier.copy(srcObj,tarObj,null);
        return tarObj;
    }

    /**
     * 对象属性复制，<b style="color: red">这是一个浅拷贝</b>
     * @param srcObj 源对象
     * @param tarClazz 目标类型
     * @param converter 对象转换器
     * @param <T> 目标泛型
     * @return 目标对象
     */
    public static <T> T copyObj(Object srcObj, Class<T> tarClazz, Converter converter){
        BeanCopier copier = BeanCopier.create(srcObj.getClass(), tarClazz, true);
        try {
            T tarObj = tarClazz.newInstance();
            copier.copy(srcObj,tarObj,converter);
            return tarObj;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new BudgetException("对象属性拷贝出错！");
    }

    /**
     * 对象属性复制，<b style="color: red">这是一个浅拷贝</b>
     * @param srcObj 源对象
     * @param tarObj 目标对象
     * @param converter 对象转换器
     * @param <T> 目标泛型
     * @return 目标对象
     */
    public static <T> T copyObj(Object srcObj, T tarObj,Converter converter){
        BeanCopier copier = BeanCopier.create(srcObj.getClass(), tarObj.getClass(), true);
        copier.copy(srcObj,tarObj,converter);
        return tarObj;
    }

    /**
     * 对象属性复制，<b style="color: red">这是一个浅拷贝</b>
     * @param srcObjs 源对象列表
     * @param tarClazz 目标类型
     * @param <T> 目标泛型
     * @return 目标对象列表
     */
    public static <T> List<T> copyObjList(List<?> srcObjs, Class<T> tarClazz){
        BeanCopier copier = BeanCopier.create(srcObjs.get(0).getClass(), tarClazz, false);
        try {
            List<T> tarObjs = new ArrayList<>(srcObjs.size());
            for (Object srcObj : srcObjs) {
                T tarObj = tarClazz.newInstance();
                copier.copy(srcObj,tarObj,null);
                tarObjs.add(tarObj);
            }
            return tarObjs;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new BudgetException("对象属性拷贝出错！");
    }

    /**
     * 对象属性复制，<b style="color: red">这是一个浅拷贝</b>
     * @param srcObjs 源对象列表
     * @param tarClazz 目标类型
     * @param converter 对象转换器
     * @param <T> 目标泛型
     * @return 目标对象列表
     */
    public static <T> List<T> copyObjList(List<?> srcObjs, Class<T> tarClazz,Converter converter){
        BeanCopier copier = BeanCopier.create(srcObjs.get(0).getClass(), tarClazz, true);
        try {
            List<T> tarObjs = new ArrayList<>(srcObjs.size());
            for (Object srcObj : srcObjs) {
                T tarObj = tarClazz.newInstance();
                copier.copy(srcObj,tarObj,converter);
                tarObjs.add(tarObj);
            }
            return tarObjs;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new BudgetException("对象属性拷贝出错！");
    }

    /**
     * 对象属性复制，<b style="color: red">这是一个浅拷贝，同时会改变传入的目标列表中的对象</b>
     * @param srcObjs 源对象列表
     * @param tarObjs 目标类型
     * @param <T> 目标泛型
     */
    public static <T> void copyObjList(List<?> srcObjs, List<T> tarObjs){
        BeanCopier copier = BeanCopier.create(srcObjs.get(0).getClass(), tarObjs.get(0).getClass(), false);
        for (int i = 0; i < srcObjs.size(); i++) {
            copier.copy(srcObjs.get(i),tarObjs.get(i),null);
        }
    }

    /**
     * 对象属性复制，<b style="color: red">这是一个浅拷贝，同时会改变传入的目标列表中的对象</b>
     * @param srcObjs 源对象列表
     * @param tarObjs 目标类型
     * @param converter 对象转换器
     * @param <T> 目标泛型
     */
    public static <T> void copyObjList(List<?> srcObjs, List<T> tarObjs,Converter converter){
        BeanCopier copier = BeanCopier.create(srcObjs.get(0).getClass(), tarObjs.get(0).getClass(), true);
        for (int i = 0; i < srcObjs.size(); i++) {
            copier.copy(srcObjs.get(i),tarObjs.get(i),converter);
        }
    }
}
