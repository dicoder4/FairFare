package com.example.billingservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document(collection = "bills")
public class Bill {

    @Id
    private String id;

    private String groupId;
    private String createdByUserId;
    private String date; // YYYY-MM-DD

    @com.fasterxml.jackson.annotation.JsonProperty("payers")
    private Map<String, Double> payers = new HashMap<>();

    public Map<String, Double> getPayers() {
        return payers;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("payers")
    public void setPayers(Map<String, Double> payers) {
        System.out.println("DEBUG: setPayers called with " + payers); // DEBUG LOG
        this.payers = payers;
    }

    private double subtotal;
    private double tax;
    private double serviceCharge;
    private double tip;
    private double total;

    private List<BillItem> items = new ArrayList<>();

    public Bill() {}

    // getters & setters

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    @Override
    public String toString() {
        return "Bill{" +
                "id='" + id + '\'' +
                ", payers=" + payers +
                ", total=" + total +
                '}';
    }
}
