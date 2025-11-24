package com.example.billingservice.repository;

import com.example.billingservice.model.BillItem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BillItemRepository extends MongoRepository<BillItem, String> {
}
