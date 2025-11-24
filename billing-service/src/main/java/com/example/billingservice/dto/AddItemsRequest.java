package com.example.billingservice.dto;

import com.example.billingservice.model.BillItem;
import java.util.List;

public class AddItemsRequest {

    private List<BillItem> items;

    public List<BillItem> getItems() {
        return items;
    }

    public void setItems(List<BillItem> items) {
        this.items = items;
    }
}
