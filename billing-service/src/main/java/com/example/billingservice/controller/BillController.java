package com.example.billingservice.controller;

import com.example.billingservice.model.Bill;
import com.example.billingservice.dto.AddItemsRequest;
import com.example.billingservice.service.BillService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bills")
public class BillController {

    @Autowired
    private BillService billService;

    // CREATE BILL
    @PostMapping
    public Bill createBill(@RequestBody Bill bill) {
        return billService.createBill(bill);
    }

    // GET BILL BY ID
    @GetMapping("/{billId}")
    public Bill getBill(@PathVariable String billId) {
        return billService.getBillById(billId);
    }

    // GET ALL BILLS
    @GetMapping
    public List<Bill> getAllBills() {
        return billService.getAllBills();
    }

    // ADD ITEMS TO BILL
    @PostMapping("/{billId}/items")
    public Bill addItems(@PathVariable String billId, @RequestBody AddItemsRequest request) {
        return billService.addItems(billId, request.getItems());
    }
}
