package com.example.billingservice.service;

import com.example.billingservice.model.Bill;
import com.example.billingservice.model.BillItem;
import com.example.billingservice.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    public Bill createBill(Bill bill) {
        calculateTotals(bill);
        return billRepository.insert(bill);
    }

    public Bill getBillById(String billId) {
        return billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found: " + billId));
    }

    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    public Bill addItems(String billId, List<BillItem> newItems) {
        Bill bill = getBillById(billId);
        List<BillItem> items = bill.getItems();
        items.addAll(newItems);
        bill.setItems(items);
        
        calculateTotals(bill);
        
        return billRepository.save(bill);
    }

    private void calculateTotals(Bill bill) {
        double subtotal = bill.getItems().stream()
                .mapToDouble(BillItem::getTotalPrice)
                .sum();

        bill.setSubtotal(subtotal);
        bill.setTotal(subtotal + bill.getTax() + bill.getServiceCharge() + bill.getTip());
    }
}
