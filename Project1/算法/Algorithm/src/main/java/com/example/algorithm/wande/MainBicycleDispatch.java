package com.example.algorithm.wande;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class MainBicycleDispatch {
    /**
     * 地铁站
     */
    private List<MetroStation> metroStations;

    /**
     * 货车
     */
    private List<Truck> trucks;

    /**
     * 小黄车移动速度
     */
    private int yellowBicycleMovingSpeed;

    /**
     * 执行分配
     *
     * @param metroStations            地铁站
     * @param trucks                   货车
     * @param yellowBicycleMovingSpeed 小黄车移动速度
     */
    public void run(List<MetroStation> metroStations, List<Truck> trucks, int yellowBicycleMovingSpeed) throws InterruptedException {
        this.metroStations = metroStations;
        this.trucks = trucks;
        this.yellowBicycleMovingSpeed = yellowBicycleMovingSpeed;
        //日志输出
        logInfo();
        //骑走小黄车
        takeBicycle();
        //货车搬运小黄车
        carryBicycle();
    }

    //搬运小黄车
    private void carryBicycle() {
    }

    /**
     * 骑走小黄车
     *
     * @throws InterruptedException
     */
    private void takeBicycle() throws InterruptedException {
        while (true) {
            Thread.sleep(1000);
            new Thread(() -> {
                MetroStation metroStation = null;
                boolean isTake = false;
                while (!isTake) {
                    //一直循环，指导能拿到车为止
                    int index = new Random().nextInt(metroStations.size());
                    metroStation = metroStations.get(index);
                    //拿到车车，并且地铁站小黄车数量减一
                    isTake = metroStation.getYellowBicycleNumbers() > 0 && metroStation.subtractionBicycleNumbers();
                }
                //乘客骑走小黄车
                Passenger passenger = new Passenger(new YellowBicycle(yellowBicycleMovingSpeed));
                //获取目的地，并设置到达目的地的距离
                passenger.setNextStationSistance(metroStation.getNeighborMetroStationDistance(metroStation.getNeighborMetroStation()));
                //一秒钟走一个格子，判断走几个格子
                int time;
                if (passenger.getNextStationSistance() % passenger.getYellowBicycle().getMovingSpeed() == 0) {
                    time = passenger.getNextStationSistance() / passenger.getYellowBicycle().getMovingSpeed();
                } else {
                    time = passenger.getNextStationSistance() / passenger.getYellowBicycle().getMovingSpeed() + 1;
                }
                for (int i = 0; i < time; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //走到目的地，目的地地铁站小黄车数量要加1
                metroStation.addOneBicycleNumbers();
            }).start();
        }
    }

    /**
     * 日志输出
     */
    private void logInfo() {
        CompletableFuture.runAsync(() -> {
            int num = 0;
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                StringBuilder builder = new StringBuilder();
                metroStations.stream().forEach(item -> {
                    builder.append(item.getName() + item.getYellowBicycleNumbers() + ",");
                });
                System.out.println(DateUtil.getTimeStrBySecond(num++) + " " + builder);
            }
        });
    }

    public static void main(String[] args) throws InterruptedException {
        List<MetroStation> metroStations = new ArrayList<>();
        MetroStation metroStationA = new MetroStation("A站车", 30);
        MetroStation metroStationB = new MetroStation("B站车", 40);
        MetroStation metroStationC = new MetroStation("C站车", 30);
        //设置相邻地铁站
        metroStationA.addNeighborMetroStation(metroStationB, 14);
        metroStationA.addNeighborMetroStation(metroStationC, 5);
        //设置相邻地铁站
        metroStationB.addNeighborMetroStation(metroStationC, 9);
        metroStationB.addNeighborMetroStation(metroStationA, 14);
        //设置相邻地铁站
        metroStationC.addNeighborMetroStation(metroStationB, 9);
        metroStationC.addNeighborMetroStation(metroStationA, 5);
        metroStations.add(metroStationA);
        metroStations.add(metroStationB);
        metroStations.add(metroStationC);

        List<Truck> trucks = new ArrayList<>();
        trucks.add(new Truck(20, 3));
        trucks.add(new Truck(20, 3));

        MainBicycleDispatch bicycleDispatch = new MainBicycleDispatch();
        bicycleDispatch.run(metroStations, trucks, 1);
    }
}