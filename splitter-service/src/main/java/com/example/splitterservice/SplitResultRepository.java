package com.example.splitterservice;

import com.example.splitterservice.model.SplitResult;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SplitResultRepository extends MongoRepository<SplitResult, String> {

    Optional<SplitResult> findByBillId(String billId);
}

