package com.eventdriven.inventoryservice.service;

import com.eventdriven.inventoryservice.domain.InventoryReservation;
import com.eventdriven.inventoryservice.domain.ProcessedEvent;
import com.eventdriven.inventoryservice.dto.OrderCreatedEvent;
import com.eventdriven.inventoryservice.repository.InventoryReservationRepository;
import com.eventdriven.inventoryservice.repository.ProcessedEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class InventoryEventService {

    private static final Logger log = LoggerFactory.getLogger(InventoryEventService.class);
    private static final String CONSUMER_NAME = "inventory-service";

    private final InventoryReservationRepository inventoryReservationRepository;
    private final ProcessedEventRepository processedEventRepository;
    private final ObjectMapper objectMapper;


    public InventoryEventService(InventoryReservationRepository inventoryReservationRepository, ProcessedEventRepository processedEventRepository, ObjectMapper objectMapper) {
        this.inventoryReservationRepository = inventoryReservationRepository;
        this.processedEventRepository = processedEventRepository;
        this.objectMapper = objectMapper;

    }

    @Transactional
    public void consumeOrderCreated(String eventKey, String payLoad) {
        log.info("InventoryEventService started for eventKey={}", eventKey);

        String idempotencyKey = (eventKey == null || eventKey.isBlank()) ? deriveKeyFromPayload(payLoad) : eventKey;

        log.info("Resolved idempotencyKey={}", idempotencyKey);

        if (processedEventRepository.existsByEventKey(idempotencyKey)) {
            log.info("Duplicate event detected, skipping eventKey={}", idempotencyKey);
            return;
        }

        OrderCreatedEvent event = parsePayLoad(payLoad);
        UUID orderId = UUID.fromString(event.getOrderId());


        List<InventoryReservation> reservations = event.getItems().stream().
                map(item -> {
                    InventoryReservation reservation = new InventoryReservation();
                    reservation.setOrderId(orderId);
                    reservation.setSku(item.getSku());
                    reservation.setQuantity(item.getQuantity());
                    reservation.setStatus("RESERVED");
                    return reservation;
                }).
                toList();

        log.info("Saving {} inventory reservations for orderId={}", reservations.size(), orderId);
       inventoryReservationRepository.saveAll(reservations);

        ProcessedEvent processedEvent = new ProcessedEvent();
        processedEvent.setEventKey(idempotencyKey);
        processedEvent.setConsumerName(CONSUMER_NAME);
        log.info("Saving processed event for eventKey={}", idempotencyKey);
        processedEventRepository.save(processedEvent);

        log.info("Inventory event processing completed for eventKey={}", idempotencyKey);

    }
    private OrderCreatedEvent parsePayLoad(String payLoad){
        try{
            return objectMapper.readValue(payLoad, OrderCreatedEvent.class);
        }catch (JsonProcessingException e){
            throw new IllegalArgumentException("Invalid event payload", e);
        }
    }

    private String deriveKeyFromPayload(String payLoad) {
        OrderCreatedEvent event = parsePayLoad(payLoad);
        return event.getOrderId();
    }
}
