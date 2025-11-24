package com.example.splitterservice.dto;

import java.util.List;

public class Bill {

    private String id;
    private String groupId;
    private String createdByUserId;

    private double subtotal;
    private double tax;
    private double serviceCharge;
    private double tip;
    private double total;

    private List<BillItem> items;

    public Bill() {}

    public String getId() {
        return id;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getTax() {
        return tax;
    }

    public double getServiceCharge() {
        return serviceCharge;
    }

    public double getTip() {
        return tip;
    }

    public double getTotal() {
        return total;
    }

    public List<BillItem> getItems() {
        return items;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public void setServiceCharge(double serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public void setTip(double tip) {
        this.tip = tip;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setItems(List<BillItem> items) {
        this.items = items;
    }
}
