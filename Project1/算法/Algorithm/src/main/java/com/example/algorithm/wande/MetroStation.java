package com.example.algorithm.wande;

import java.util.*;

/**
 * 地铁站
 */
public class MetroStation {
    /**
     * 车站名称
     */
    private String name;
    /**
     * 现存小黄车数量
     */
    private List<YellowBicycle> yellowBicycles;

    private volatile int yellowBicycleNumbers;

    private Map<MetroStation, Integer> neighborMetroStationDistance;

    public MetroStation(String name, int yellowBicycleNumbers) {
        this.name = name;
        this.yellowBicycleNumbers = yellowBicycleNumbers;
        yellowBicycles = new ArrayList<>();
        neighborMetroStationDistance = new HashMap<>();
    }

    /**
     * 添加相邻地铁站
     *
     * @param metroStation
     * @param distance
     */
    public void addNeighborMetroStation(MetroStation metroStation, int distance) {
        if (neighborMetroStationDistance.containsKey(metroStation)) {
            return;
        }
        neighborMetroStationDistance.put(metroStation, distance);
    }

    public int getYellowBicycleNumbers() {
        return yellowBicycleNumbers;
    }

    public void setYellowBicycleNumbers(int yellowBicycleNumbers) {
        this.yellowBicycleNumbers = yellowBicycleNumbers;
    }

    /**
     * 小黄车被骑走一个
     *
     * @return
     */
    public synchronized boolean subtractionBicycleNumbers() {
        if (this.yellowBicycleNumbers > 0) {
            this.yellowBicycleNumbers = this.yellowBicycleNumbers - 1;
            return true;
        }
        return false;
    }

    /**
     * 车辆数量加一
     */
    public synchronized void addOneBicycleNumbers() {
        this.yellowBicycleNumbers = this.yellowBicycleNumbers + 1;
    }

    /**
     * 获取相邻的地铁站
     *
     * @return
     */
    public MetroStation getNeighborMetroStation() {
        MetroStation[] keys = neighborMetroStationDistance.keySet().toArray(new MetroStation[0]);
        int index = new Random().nextInt(keys.length);
        return keys[index];
    }

    /**
     * 获取相邻地铁站的距离
     *
     * @param metroStation
     * @return
     */
    public int getNeighborMetroStationDistance(MetroStation metroStation) {
        return neighborMetroStationDistance.get(metroStation);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}