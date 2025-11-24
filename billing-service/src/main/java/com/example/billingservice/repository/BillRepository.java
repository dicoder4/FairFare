package com.example.billingservice.repository;

import com.example.billingservice.model.Bill;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BillRepository extends MongoRepository<Bill, String> {
}
