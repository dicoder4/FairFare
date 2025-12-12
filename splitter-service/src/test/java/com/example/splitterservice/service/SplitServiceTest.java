package com.example.splitterservice.service;

import com.example.splitterservice.SplitResultRepository;
import com.example.splitterservice.dto.Bill;
import com.example.splitterservice.dto.BillItem;
import com.example.splitterservice.model.SplitResult;
import com.example.splitterservice.model.UserShare;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;


import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SplitServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private SplitResultRepository splitResultRepository;

    @InjectMocks
    private SplitService splitService;

    private Bill mockBill;

    @BeforeEach
    void setUp() {
        mockBill = new Bill();
        mockBill.setId("b1");
        mockBill.setGroupId("g1");
        mockBill.setPaidByUserId("alice");
        mockBill.setTax(10.0);
        mockBill.setTip(0.0);
        mockBill.setServiceCharge(0.0);
        
        BillItem item1 = new BillItem();
        item1.setTotalPrice(100.0);
        item1.setAssignedUserIds(Arrays.asList("alice", "bob"));
        
        mockBill.setItems(Collections.singletonList(item1));
        mockBill.setTotal(110.0); // 100 + 10 tax
    }

    @Test
    void getOrCalculateSplit_WhenExists_ShouldReturnFromRepo() {
        // Given
        SplitResult existing = new SplitResult();
        existing.setBillId("b1");
        when(splitResultRepository.findByBillId("b1")).thenReturn(Optional.of(existing));

        // When
        SplitResult result = splitService.getOrCalculateSplit("b1");

        // Then
        assertThat(result).isSameAs(existing);
        verify(restTemplate, never()).getForObject(anyString(), eq(Bill.class));
    }

    @Test
    void getOrCalculateSplit_WhenMissing_ShouldFetchAndCalculate() {
        // Given
        when(splitResultRepository.findByBillId("b1")).thenReturn(Optional.empty());
        when(restTemplate.getForObject("http://billing-service/bills/b1", Bill.class)).thenReturn(mockBill);
        when(splitResultRepository.save(any(SplitResult.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        SplitResult result = splitService.getOrCalculateSplit("b1");

        // Then
        // Item cost $100. Alice and Bob share. Each $50.
        // Tax $10. Alice and Bob share. Each $5.
        // Total Owed: Alice $55, Bob $55.
        // Paid By: Alice.
        // Alice: Owed 55. Paid 110. Net = 55 - 110 = -55 (Is Owed 55).
        // Bob: Owed 55. Paid 0. Net = 55 (Owes 55).
        
        assertThat(result.getUserShares()).hasSize(2);
        
        UserShare alice = result.getUserShares().stream().filter(s -> s.getUserId().equals("alice")).findFirst().get();
        UserShare bob = result.getUserShares().stream().filter(s -> s.getUserId().equals("bob")).findFirst().get();
        
        // Assert Alice (Payer)
        assertThat(alice.getTotalOwed()).isEqualTo(55.0);
        assertThat(alice.getPaidAmount()).isEqualTo(110.0);
        assertThat(alice.getNetBalance()).isEqualTo(-55.0);
        
        // Assert Bob (Consumer)
        assertThat(bob.getTotalOwed()).isEqualTo(55.0);
        assertThat(bob.getPaidAmount()).isEqualTo(0.0);
        assertThat(bob.getNetBalance()).isEqualTo(55.0); // Owes
        
        verify(splitResultRepository).save(any(SplitResult.class));
    }

    @Test
    void settleDebt_ShouldUpdateSettledFlag() {
        // Given
        SplitResult initial = new SplitResult();
        initial.setBillId("b1");
        UserShare bobShare = new UserShare("bob", 50, 5, 0, 0, 55, 0, 55, false);
        initial.setUserShares(Collections.singletonList(bobShare));
        
        when(splitResultRepository.findByBillId("b1")).thenReturn(Optional.of(initial));
        when(splitResultRepository.save(any(SplitResult.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        SplitResult updated = splitService.settleDebt("b1", "bob");

        // Then
        UserShare updatedBob = updated.getUserShares().get(0);
        assertThat(updatedBob.isSettled()).isTrue();
        verify(splitResultRepository).save(initial);
    }
}
