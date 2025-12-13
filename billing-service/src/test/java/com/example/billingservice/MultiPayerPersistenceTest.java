package com.example.billingservice;

import com.example.billingservice.model.Bill;
import com.example.billingservice.repository.BillRepository;
import com.example.billingservice.service.BillService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@Import(BillService.class)
public class MultiPayerPersistenceTest {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BillService billService;

    @Test
    public void testPayersMapPersistence() {
        // 1. Create Bill with Multi-Payer Data
        Bill bill = new Bill();
        bill.setGroupId("group_test_1");
        bill.setTax(10.0);
        bill.setTotal(110.0);
        
        Map<String, Double> payers = new HashMap<>();
        payers.put("alice_id", 60.0);
        payers.put("bob_id", 50.0);
        bill.setPayers(payers);

        // 2. Save via Service
        Bill saved = billService.createBill(bill);

        // 3. Verify ID generated
        assertThat(saved.getId()).isNotNull();

        // 4. Retrieve from DB
        Bill retrieved = billRepository.findById(saved.getId()).orElse(null);
        assertThat(retrieved).isNotNull();
        
        // 5. Verify Map Content
        assertThat(retrieved.getPayers()).isNotNull();
        assertThat(retrieved.getPayers()).hasSize(2);
        assertThat(retrieved.getPayers().get("alice_id")).isEqualTo(60.0);
        assertThat(retrieved.getPayers().get("bob_id")).isEqualTo(50.0);

        System.out.println("Persistence Test Passed: Payers map saved and retrieved successfully.");
    }
}
