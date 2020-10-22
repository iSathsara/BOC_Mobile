package com.example.boc_mobile;

public class Transactions {

    public String uname;
    public String customerName;
    public String method;
    public int amount;
    public String payeeAcc;
    public String payeeName;
    public String invoice;
    public String description;

    public Transactions() {

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
