package com.bitproject.fahim.homeservice.classes;

public class SPAccount {
    private String account_id, sp_id;
    private double due_balance, paid_balance, total_income;
    private String name, email;

    public SPAccount() {
    }

    public SPAccount(String account_id, String sp_id, double due_balance, double paid_balance, double total_income) {
        this.account_id = account_id;
        this.sp_id = sp_id;
        this.due_balance = due_balance;
        this.paid_balance = paid_balance;
        this.total_income = total_income;
    }

    public SPAccount(String account_id, String sp_id, double due_balance, double paid_balance, String name, String email) {
        this.account_id = account_id;
        this.sp_id = sp_id;
        this.due_balance = due_balance;
        this.paid_balance = paid_balance;
        this.name = name;
        this.email = email;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getSp_id() {
        return sp_id;
    }

    public void setSp_id(String sp_id) {
        this.sp_id = sp_id;
    }

    public double getDue_balance() {
        return due_balance;
    }

    public void setDue_balance(double due_balance) {
        this.due_balance = due_balance;
    }

    public double getPaid_balance() {
        return paid_balance;
    }

    public void setPaid_balance(double paid_balance) {
        this.paid_balance = paid_balance;
    }

    public double getTotal_income() {
        return total_income;
    }

    public void setTotal_income(double total_income) {
        this.total_income = total_income;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
