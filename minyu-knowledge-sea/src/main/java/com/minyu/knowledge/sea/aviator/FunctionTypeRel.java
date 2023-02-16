package com.minyu.knowledge.sea.aviator;

import com.minyu.knowledge.sea.aviator.sort.AbsoluteSort;
import com.minyu.knowledge.sea.aviator.sort.RelevantSort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: TODO
 * @Author: lijingyu
 * @CreateTime: 2023-02-16  23:04
 */
public class FunctionTypeRel {
    public static Map<String, Class> functionRelMap = new HashMap<>();
    public static List<String> categoryList = new ArrayList<>();

    static {
        functionRelMap.put("absolute", AbsoluteSort.class);
        functionRelMap.put("relevant", RelevantSort.class);
        categoryList.add("absolute");
        categoryList.add("relevant");
    }
}