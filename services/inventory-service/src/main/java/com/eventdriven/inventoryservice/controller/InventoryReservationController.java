package com.eventdriven.inventoryservice.controller;

import com.eventdriven.inventoryservice.domain.InventoryReservation;
import com.eventdriven.inventoryservice.repository.InventoryReservationRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/inventory/reservations")
public class InventoryReservationController {

    private final InventoryReservationRepository inventoryReservationRepository;

    public InventoryReservationController(InventoryReservationRepository inventoryReservationRepository) {
        this.inventoryReservationRepository = inventoryReservationRepository;
    }

    @GetMapping("/{orderId}")
    public List<InventoryReservation> getCreatedOrder(@PathVariable UUID orderId){

       return inventoryReservationRepository.findByOrderId(orderId);

    }
}
