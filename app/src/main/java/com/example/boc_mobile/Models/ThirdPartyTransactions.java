package com.example.boc_mobile.Models;

public class ThirdPartyTransactions {

    public String payee;
    public String uname;
    public String name;
    public int amount;
    public String description;

    public ThirdPartyTransactions(){

    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

