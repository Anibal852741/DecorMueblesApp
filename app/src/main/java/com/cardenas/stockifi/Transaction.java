package com.cardenas.stockifi;

import java.io.Serializable;

public class Transaction implements Serializable {
    private long date;
    private String productName;
    private int quantity;
    private double totalPrice;

    public Transaction(long date, String productName, int quantity, double totalPrice) {
        this.date = date;
        this.productName = productName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
