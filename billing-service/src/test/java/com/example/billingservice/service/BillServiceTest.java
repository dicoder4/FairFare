package com.example.billingservice.service;

import com.example.billingservice.model.Bill;
import com.example.billingservice.model.BillItem;
import com.example.billingservice.repository.BillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BillServiceTest {

    @Mock
    private BillRepository billRepository;

    @InjectMocks
    private BillService billService;

    private Bill bill;

    @BeforeEach
    void setUp() {
        bill = new Bill();
        bill.setId("b1");
        bill.setItems(new ArrayList<>());
        bill.setTax(10.0);
    }

    @Test
    void createBill_ShouldCalculateTotals() {
        // Given
        BillItem item = new BillItem();
        item.setUnitPrice(100.0);
        item.setQuantity(1); // subtotal = 100
        bill.getItems().add(item);
        
        when(billRepository.insert(any(Bill.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        Bill saved = billService.createBill(bill);

        // Then
        assertThat(saved.getSubtotal()).isEqualTo(100.0);
        assertThat(saved.getTotal()).isEqualTo(110.0); // 100 + 10 tax
        verify(billRepository).insert(bill);
    }

    @Test
    void addItems_ShouldRecalculateTotals() {
        // Given
        // Existing bill with 0 items
        when(billRepository.findById("b1")).thenReturn(Optional.of(bill));
        when(billRepository.save(any(Bill.class))).thenAnswer(i -> i.getArguments()[0]);

        BillItem newItem = new BillItem();
        newItem.setUnitPrice(50);
        newItem.setQuantity(2); // 100

        // When
        Bill updated = billService.addItems("b1", Collections.singletonList(newItem));

        // Then
        assertThat(updated.getItems()).hasSize(1);
        assertThat(updated.getSubtotal()).isEqualTo(100.0);
        assertThat(updated.getTotal()).isEqualTo(110.0); // 100 + 10 tax
    }
}
