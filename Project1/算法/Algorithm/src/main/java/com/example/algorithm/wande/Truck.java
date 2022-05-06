package com.example.algorithm.wande;

import java.util.ArrayList;
import java.util.List;

/**
 * 货车
 */
public class Truck {
    /**
     * 可装载小黄车的数量
     */
    private List<YellowBicycle> yellowBicycles;

    /**
     * 可装载小黄车的最大数量
     */
    private int maxYellowBicycleNumbers;

    /**
     * 移动速度
     */
    private int movingSpeed;

    /**
     * 距离下一个地铁站的距离
     */
    private int nextStationSistance;

    public Truck(int maxYellowBicycleNumbers, int movingSpeed) {
        this.maxYellowBicycleNumbers = maxYellowBicycleNumbers;
        this.yellowBicycles = new ArrayList<>();
        this.movingSpeed = movingSpeed;
    }
}