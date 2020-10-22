package com.example.boc_mobile.Models;

public class PaidBills {

    String customerId;
    String biller;
    String accNo;
    String invoice;
    int amount;


    public PaidBills() {

    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getBiller() {
        return biller;
    }

    public String getInvoice() {
        return invoice;
    }

    public int getAmount() {
        return amount;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setBiller(String biller) {
        this.biller = biller;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getCustomerId() {
        return customerId;
    }
}
