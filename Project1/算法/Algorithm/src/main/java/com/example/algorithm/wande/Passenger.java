package com.example.algorithm.wande;

/**
 * 乘客
 */
public class Passenger {
    /**
     * 小黄车
     */
    private YellowBicycle yellowBicycle;

    /**
     * 距离下一个地铁站的距离
     */
    private int nextStationSistance;

    public Passenger(YellowBicycle yellowBicycle) {
        this.yellowBicycle = yellowBicycle;
    }

    public int getNextStationSistance() {
        return nextStationSistance;
    }

    public void setNextStationSistance(int nextStationSistance) {
        this.nextStationSistance = nextStationSistance;
    }

    public YellowBicycle getYellowBicycle() {
        return yellowBicycle;
    }

    public void setYellowBicycle(YellowBicycle yellowBicycle) {
        this.yellowBicycle = yellowBicycle;
    }
}