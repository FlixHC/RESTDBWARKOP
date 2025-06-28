package com.mycompany.mavenproject3;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Transaction {
    private int transactionId;
    private String customerName;
    private List<TransactionItem> items;
    private LocalDateTime transactionDateTime; // New field to store date and time

    public Transaction(int transactionId, LocalDateTime transactionDateTime, String customerName) {
        this.transactionId = transactionId;
        this.customerName = customerName;
        this.items = new ArrayList<>();
        this.transactionDateTime = LocalDateTime.now(); // Record current date and time
    }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String name) { this.customerName = name; }

    public List<TransactionItem> getItems() { return items; }
    public void addItem(TransactionItem item) { this.items.add(item); }

    public double getTotalPrice() {
        return items.stream().mapToDouble(TransactionItem::getTotalPrice).sum();
    }

    public LocalDateTime getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(LocalDateTime transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }
}
