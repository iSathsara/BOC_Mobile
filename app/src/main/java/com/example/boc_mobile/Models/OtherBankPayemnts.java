package com.example.boc_mobile.Models;

public class OtherBankPayemnts {

    public String uname;
    public String customerName;
    public int amount;
    public String description;
    public String payee;

    public  OtherBankPayemnts() {

    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
