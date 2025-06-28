package com.mycompany.mavenproject3;

public class TransactionItem {
    private String productName;
    private int qty;
    private double price;

    public TransactionItem(String productName, int qty, double price) {
        this.productName = productName;
        this.qty = qty;
        this.price = price;
    }

    public String getProductName() { return productName; }
    public int getQty() { return qty; }
    public double getPrice() { return price; }
    public double getTotalPrice() { return qty * price; }
}
