package com.bitproject.fahim.homeservice.classes;

public class CookingPrice {
    private double oneToFourDaily, oneToFourMonthly, fiveToTenDaily, fiveToTenMonthly;

    public CookingPrice() {
    }

    public CookingPrice(double oneToFourDaily, double oneToFourMonthly, double fiveToTenDaily, double fiveToTenMonthly) {
        this.oneToFourDaily = oneToFourDaily;
        this.oneToFourMonthly = oneToFourMonthly;
        this.fiveToTenDaily = fiveToTenDaily;
        this.fiveToTenMonthly = fiveToTenMonthly;
    }

    public double getOneToFourDaily() {
        return oneToFourDaily;
    }

    public void setOneToFourDaily(double oneToFourDaily) {
        this.oneToFourDaily = oneToFourDaily;
    }

    public double getOneToFourMonthly() {
        return oneToFourMonthly;
    }

    public void setOneToFourMonthly(double oneToFourMonthly) {
        this.oneToFourMonthly = oneToFourMonthly;
    }

    public double getFiveToTenDaily() {
        return fiveToTenDaily;
    }

    public void setFiveToTenDaily(double fiveToTenDaily) {
        this.fiveToTenDaily = fiveToTenDaily;
    }

    public double getFiveToTenMonthly() {
        return fiveToTenMonthly;
    }

    public void setFiveToTenMonthly(double fiveToTenMonthly) {
        this.fiveToTenMonthly = fiveToTenMonthly;
    }
}
