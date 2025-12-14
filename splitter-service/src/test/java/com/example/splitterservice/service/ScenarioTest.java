package com.example.splitterservice.service;

import com.example.splitterservice.SplitResultRepository;
import com.example.splitterservice.dto.Bill;
import com.example.splitterservice.dto.BillItem;
import com.example.splitterservice.model.SplitResult;
import com.example.splitterservice.model.UserShare;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
class ScenarioTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private SplitResultRepository splitResultRepository;

    @InjectMocks
    private SplitService splitService;

    @Test
    void testAliceAndCharliePaymentScenario() {
        // --- SCENARIO DATA ---
        // Bill Total: 550
        // Item: 500 (Shared by Alice, Bob, Charlie)
        // Tax: 50
        // Payers: Alice (200), Charlie (350)
        
        Bill bill = new Bill();
        bill.setId("scenario_bill");
        bill.setGroupId("grp1");
        bill.setTax(50.0);
        
        // Items
        BillItem item = new BillItem();
        item.setName("Expensive Dinner");
        item.setTotalPrice(500.0);
        item.setAssignedUserIds(Arrays.asList("alice", "bob", "charlie"));
        bill.setItems(Arrays.asList(item));
        
        // Payers
        Map<String, Double> payers = new HashMap<>();
        payers.put("alice", 200.0);
        payers.put("charlie", 350.0);
        bill.setPayers(payers);

        // Mock RestTemplate to return this bill
        when(restTemplate.getForObject(anyString(), eq(Bill.class))).thenReturn(bill);
        
        // Mock Repository to save and return
        when(splitResultRepository.save(any(SplitResult.class))).thenAnswer(i -> i.getArguments()[0]);

        // --- EXECUTE ---
        SplitResult result = splitService.calculateAndSaveSplit("scenario_bill");

        // --- ASSERTIONS ---
        assertThat(result).isNotNull();
        
        // 1. Total Cost Per Person: (500 + 50) / 3 = 183.333...
        double expectedCost = 550.0 / 3.0; // ~183.33

        // 2. Verify Alice
        UserShare alice = result.getUserShares().stream().filter(u -> u.getUserId().equals("alice")).findFirst().orElseThrow();
        assertThat(alice.getTotalOwed()).isCloseTo(expectedCost, org.assertj.core.data.Offset.offset(0.01));
        assertThat(alice.getPaidAmount()).isEqualTo(200.0);
        // Balance: 183.33 - 200 = -16.67 (Negative means Owed money? No, wait.)
        // Logic check: Balance = Cost - Paid. 
        // 183.33 - 200 = -16.67. 
        // Is system "Negative = Credit"? 
        // Check code: double netBalance = totalOwed - paidAmount;
        // Yes, verify file content: Line 79 of SplitService: double netBalance = totalOwed - paidAmount;
        // So standard is: Positive = Owe, Negative = Owed.
        assertThat(alice.getNetBalance()).isCloseTo(expectedCost - 200.0, org.assertj.core.data.Offset.offset(0.01));

        // 3. Verify Charlie
        UserShare charlie = result.getUserShares().stream().filter(u -> u.getUserId().equals("charlie")).findFirst().orElseThrow();
        assertThat(charlie.getTotalOwed()).isCloseTo(expectedCost, org.assertj.core.data.Offset.offset(0.01));
        assertThat(charlie.getPaidAmount()).isEqualTo(350.0);
        assertThat(charlie.getNetBalance()).isCloseTo(expectedCost - 350.0, org.assertj.core.data.Offset.offset(0.01));

        // 4. Verify Bob
        UserShare bob = result.getUserShares().stream().filter(u -> u.getUserId().equals("bob")).findFirst().orElseThrow();
        assertThat(bob.getTotalOwed()).isCloseTo(expectedCost, org.assertj.core.data.Offset.offset(0.01));
        assertThat(bob.getPaidAmount()).isEqualTo(0.0);
        assertThat(bob.getNetBalance()).isCloseTo(expectedCost, org.assertj.core.data.Offset.offset(0.01)); // Bob Owes full amount
        
        System.out.println("Scenario Validated Successfully!");
    }
    }

    @Test
    void testPartialSettlement() {
        // Given a stored split result where Bob owes money
        SplitResult storedSplit = new SplitResult();
        storedSplit.setBillId("settle_bill");
        
        // Bob owes 100
        UserShare bob = new UserShare("bob", 100.0, 0,0,0, 100.0, 0.0, 100.0, new java.util.HashSet<>());
        storedSplit.setUserShares(Arrays.asList(bob));
        
        when(splitResultRepository.findByBillId("settle_bill")).thenReturn(java.util.Optional.of(storedSplit));
        when(splitResultRepository.save(any(SplitResult.class))).thenAnswer(i -> i.getArguments()[0]);

        // When Bob pays Alice
        SplitResult result1 = splitService.settleDebt("settle_bill", "bob", "alice");
        
        // Then Bob should satisfy Alice
        UserShare bobAfter1 = result1.getUserShares().get(0);
        assertThat(bobAfter1.getSettledWithUserIds()).contains("alice");
        assertThat(bobAfter1.getSettledWithUserIds()).doesNotContain("charlie");
        
        // When Bob pays Charlie
        SplitResult result2 = splitService.settleDebt("settle_bill", "bob", "charlie");
        
        // Then Bob should satisfy both
        UserShare bobAfter2 = result2.getUserShares().get(0);
        assertThat(bobAfter2.getSettledWithUserIds()).contains("alice", "charlie");
    }
}
