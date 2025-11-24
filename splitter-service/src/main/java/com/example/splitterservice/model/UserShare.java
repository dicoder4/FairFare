package com.example.splitterservice.model;

public class UserShare {

    private String userId;
    private double itemsSubtotal;
    private double taxShare;
    private double serviceChargeShare;
    private double tipShare;
    private double totalOwed;

    public UserShare() {}

    public UserShare(String userId,
                     double itemsSubtotal,
                     double taxShare,
                     double serviceChargeShare,
                     double tipShare,
                     double totalOwed) {
        this.userId = userId;
        this.itemsSubtotal = itemsSubtotal;
        this.taxShare = taxShare;
        this.serviceChargeShare = serviceChargeShare;
        this.tipShare = tipShare;
        this.totalOwed = totalOwed;
    }

    public String getUserId() {
        return userId;
    }

    public double getItemsSubtotal() {
        return itemsSubtotal;
    }

    public double getTaxShare() {
        return taxShare;
    }

    public double getServiceChargeShare() {
        return serviceChargeShare;
    }

    public double getTipShare() {
        return tipShare;
    }

    public double getTotalOwed() {
        return totalOwed;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setItemsSubtotal(double itemsSubtotal) {
        this.itemsSubtotal = itemsSubtotal;
    }

    public void setTaxShare(double taxShare) {
        this.taxShare = taxShare;
    }

    public void setServiceChargeShare(double serviceChargeShare) {
        this.serviceChargeShare = serviceChargeShare;
    }

    public void setTipShare(double tipShare) {
        this.tipShare = tipShare;
    }

    public void setTotalOwed(double totalOwed) {
        this.totalOwed = totalOwed;
    }
}
