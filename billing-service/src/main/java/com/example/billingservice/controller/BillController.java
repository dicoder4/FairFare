package com.example.billingservice.controller;

import com.example.billingservice.model.Bill;
import com.example.billingservice.repository.BillRepository;
import com.example.billingservice.dto.AddItemsRequest;
import com.example.billingservice.model.BillItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bills")
public class BillController {

    @Autowired
    private BillRepository billRepository;

    // CREATE BILL
    @PostMapping
    public Bill createBill(@RequestBody Bill bill) {

        double subtotal = bill.getItems().stream()
                              .mapToDouble(BillItem::getTotalPrice)
                              .sum();

        bill.setSubtotal(subtotal);
        bill.setTotal(subtotal + bill.getTax() + bill.getServiceCharge() + bill.getTip());

        return billRepository.insert(bill);
    }

    // GET BILL BY ID
    @GetMapping("/{billId}")
    public Bill getBill(@PathVariable String billId) {
        return billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found: " + billId));
    }

    // ADD ITEMS TO BILL
    @PostMapping("/{billId}/items")
    public Bill addItems(@PathVariable String billId, @RequestBody AddItemsRequest request) {

        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        List<BillItem> items = bill.getItems();
        items.addAll(request.getItems());

        double subtotal = items.stream().mapToDouble(BillItem::getTotalPrice).sum();

        bill.setSubtotal(subtotal);
        bill.setTotal(subtotal + bill.getTax() + bill.getServiceCharge() + bill.getTip());

        bill.setItems(items);
        return billRepository.save(bill);
    }
}
