package com.eventdriven.inventoryservice.repository;

import com.eventdriven.inventoryservice.domain.InventoryReservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InventoryReservationRepository extends JpaRepository<InventoryReservation, UUID> {

    List<InventoryReservation> findByOrderId(UUID order_id);
}
