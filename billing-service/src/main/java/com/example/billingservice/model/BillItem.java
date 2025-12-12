package com.example.billingservice.model;

import org.springframework.data.annotation.Id;
import java.util.ArrayList;
import java.util.List;

public class BillItem {

    @Id
    private String id;

    private String name;
    private int quantity;
    private double unitPrice;
    private double totalPrice;

    private List<String> assignedUserIds = new ArrayList<>();

    public BillItem() {}

    public BillItem(String name, int quantity, double unitPrice, List<String> assignedUserIds) {
        this.name = name;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = quantity * unitPrice;
        this.assignedUserIds = assignedUserIds == null ? new ArrayList<>() : assignedUserIds;
    }

    // getters & setters

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

    public void setAssignedUserIds(List<String> assignedUserIds) {
        this.assignedUserIds = assignedUserIds;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.totalPrice = this.quantity * this.unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        this.totalPrice = this.quantity * this.unitPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
