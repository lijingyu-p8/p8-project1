package com.jiuqi.budget.common.utils;

/**
 * 控制Map中初始元素个数的util，避免在List数据量过大时，Map频繁resize
 * @author wangxing
 */
public class MapSizeUtil {

    /**
     * map size的默认值范围
     */
    private static final int[] DEFAULT_INITIAL_CAPACITIES =
            new int[]{1 << 1, 1 << 2, 1 << 3, 1 << 4, 1 << 5, 1 << 6, 1 << 7, 1 << 8, 1 << 9, 1 << 10,
                    1 << 11, 1 << 12, 1 << 13, 1 << 14, 1 << 15, 1 << 16, 1 << 17, 1 << 18, 1 << 19, 1 << 20,
                    1 << 21, 1 << 22, 1 << 23, 1 << 24, 1 << 25, 1 << 26, 1 << 27, 1 << 28, 1 << 29, 1 << 30};
    /**
     * map默认值的最大值
     */
    private static final int MAXIMUM_CAPACITY = 1 << 30;
	private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
	private static final float DEFAULT_LOAD_FACTOR = 0.75f;

	/**
	 * 根据列表长度返回合适的hashMap长度
	 * @param listSize
	 * @return
	 */
	public static int getHashMapInitCapacities(int listSize){
		if(listSize==0){
			return DEFAULT_INITIAL_CAPACITY;
		}
		float listSize_f = listSize/DEFAULT_LOAD_FACTOR;
		for(int i = 0; i < 30; i++) {
            if (DEFAULT_INITIAL_CAPACITIES[i] > listSize_f) {
                return DEFAULT_INITIAL_CAPACITIES[i];
			}
		}
		return MAXIMUM_CAPACITY;
	}

}
