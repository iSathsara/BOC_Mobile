package com.example.boc_mobile.Models;

public class CreditCardPayments {

    public String uname;
    public String customerName;
    public String method;
    public int amount;
    public String description;
    public String payee;

    public CreditCardPayments() {

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

    public void setMethod(String method) {
        this.method = method;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }



}
