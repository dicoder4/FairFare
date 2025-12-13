package com.example.splitterservice.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bill {

    private String id;
    private String groupId;
    private String createdByUserId;
    
    // Multi-payer support
    private Map<String, Double> payers = new HashMap<>();

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

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public Map<String, Double> getPayers() {
        return payers;
    }

    public void setPayers(Map<String, Double> payers) {
        this.payers = payers;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(double serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public double getTip() {
        return tip;
    }

    public void setTip(double tip) {
        this.tip = tip;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<BillItem> getItems() {
        return items;
    }

    public void setItems(List<BillItem> items) {
        this.items = items;
    }
}
