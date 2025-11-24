package com.example.splitterservice.dto;

import java.util.List;

public class BillItem {

    private String id;
    private String name;
    private int quantity;
    private double unitPrice;
    private double totalPrice;

    // userIds who share this item (equal split)
    private List<String> assignedUserIds;

    public BillItem() {}

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public List<String> getAssignedUserIds() {
        return assignedUserIds;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setAssignedUserIds(List<String> assignedUserIds) {
        this.assignedUserIds = assignedUserIds;
    }
}
