package com.bitproject.fahim.homeservice.classes;

public class AdminAccount {
    private String account_id;
    private double total_income;

    public AdminAccount() {
    }

    public AdminAccount(String account_id, double total_income) {
        this.account_id = account_id;
        this.total_income = total_income;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public double getTotal_income() {
        return total_income;
    }

    public void setTotal_income(double total_income) {
        this.total_income = total_income;
    }
}
