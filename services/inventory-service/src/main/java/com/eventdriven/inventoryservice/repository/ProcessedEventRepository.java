package com.eventdriven.inventoryservice.repository;

import com.eventdriven.inventoryservice.domain.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessedEventRepository extends JpaRepository<ProcessedEvent, String> {


    boolean existsByEventKey(String idempotencyKey);

}
