package com.example.splitterservice.service;

import com.example.splitterservice.SplitResultRepository;
import com.example.splitterservice.dto.Bill;
import com.example.splitterservice.dto.BillItem;
import com.example.splitterservice.model.SplitResult;
import com.example.splitterservice.model.UserShare;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class SplitService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SplitResultRepository splitResultRepository;

    private static final String BILLING_SERVICE_BASE_URL = "http://localhost:3081";

    public SplitResult getOrCalculateSplit(String billId) {
        return splitResultRepository.findByBillId(billId)
                .orElseGet(() -> calculateAndSaveSplit(billId));
    }

    public SplitResult calculateAndSaveSplit(String billId) {
        String url = BILLING_SERVICE_BASE_URL + "/bills/" + billId;
        Bill bill = restTemplate.getForObject(url, Bill.class);

        if (bill == null) {
            throw new RuntimeException("Bill not found from billing-service for id: " + billId);
        }

        Map<String, Double> userItemTotals = new HashMap<>();

        if (bill.getItems() != null) {
            for (BillItem item : bill.getItems()) {
                List<String> assignedUsers = item.getAssignedUserIds();
                if (assignedUsers == null || assignedUsers.isEmpty()) {
                    continue; // item not assigned to anyone -> ignore for now
                }

                double perUserShare = item.getTotalPrice() / assignedUsers.size();

                for (String userId : assignedUsers) {
                    userItemTotals.merge(userId, perUserShare, Double::sum);
                }
            }
        }

        double totalItemsSubtotal = userItemTotals.values().stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        List<UserShare> shares = new ArrayList<>();

        for (Map.Entry<String, Double> entry : userItemTotals.entrySet()) {
            String userId = entry.getKey();
            double userSubtotal = entry.getValue();

            double ratio = (totalItemsSubtotal > 0) ? (userSubtotal / totalItemsSubtotal) : 0.0;

            double taxShare = ratio * bill.getTax();
            double serviceShare = ratio * bill.getServiceCharge();
            double tipShare = ratio * bill.getTip();

            double totalOwed = userSubtotal + taxShare + serviceShare + tipShare;

            UserShare userShare = new UserShare(
                    userId,
                    userSubtotal,
                    taxShare,
                    serviceShare,
                    tipShare,
                    totalOwed
            );

            shares.add(userShare);
        }

        SplitResult result = new SplitResult(bill.getId(), bill.getGroupId(), shares);
        return splitResultRepository.save(result);
    }
}
