package com.example.splitterservice.controller;

import com.example.splitterservice.model.SplitResult;
import com.example.splitterservice.service.SplitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/splits")
public class SplitController {

    @Autowired
    private SplitService splitService;

    // GET /splits/{billId} -> either existing or newly computed split
    @GetMapping("/{billId}")
    public SplitResult getSplitForBill(@PathVariable String billId) {
        return splitService.getOrCalculateSplit(billId);
    }

    // POST /splits/{billId}/recalculate -> force recomputation
    @PostMapping("/{billId}/recalculate")
    public SplitResult recalculate(@PathVariable String billId) {
        return splitService.calculateAndSaveSplit(billId);
    }

    @PostMapping("/{billId}/settle/{userId}")
    public SplitResult settleDebt(@PathVariable String billId, 
                                  @PathVariable String userId,
                                  @RequestParam(required = false) String creditorId) {
        return splitService.settleDebt(billId, userId, creditorId);
    }
}
