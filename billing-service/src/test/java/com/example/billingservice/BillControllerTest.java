package com.example.billingservice;

import com.example.billingservice.controller.BillController;
import com.example.billingservice.model.Bill;
import com.example.billingservice.service.BillService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BillController.class)
public class BillControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BillService billService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateBillWithPayersPayload() throws Exception {
        // 1. Construct JSON Payload similar to Frontend
        // { "groupId": "g1", "payers": { "alice": 50.0, "bob": 50.0 }, "total": 100.0 }
        
        Map<String, Object> payload = new HashMap<>();
        payload.put("groupId", "g1");
        payload.put("total", 100.0);
        
        Map<String, Double> payers = new HashMap<>();
        payers.put("alice", 50.0);
        payers.put("bob", 50.0);
        payload.put("payers", payers);

        String json = objectMapper.writeValueAsString(payload);
        System.out.println("Testing Payload: " + json);

        // 2. Perform POST
        mockMvc.perform(post("/bills")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());

        // 3. Capture Argument passed to Service
        ArgumentCaptor<Bill> captor = ArgumentCaptor.forClass(Bill.class);
        verify(billService).createBill(captor.capture());
        Bill capturedBill = captor.getValue();

        // 4. Verify Payers Map populated
        assertThat(capturedBill.getPayers()).isNotNull();
        assertThat(capturedBill.getPayers()).hasSize(2);
        assertThat(capturedBill.getPayers().get("alice")).isEqualTo(50.0);
        
        System.out.println("Controller Test Passed: JSON payload correctly mapped to Bill object.");
    }
}
