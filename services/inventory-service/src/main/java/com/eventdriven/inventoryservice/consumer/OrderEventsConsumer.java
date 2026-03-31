package com.eventdriven.inventoryservice.consumer;

import com.eventdriven.inventoryservice.service.InventoryEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class OrderEventsConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderEventsConsumer.class);
    private final InventoryEventService inventoryEventService;


    public OrderEventsConsumer(InventoryEventService inventoryEventService) {
        this.inventoryEventService = inventoryEventService;
    }

    @KafkaListener(topics = "order-events")
    public void handleOrderCreated(
            @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) String key,
            @Payload String payload
    ) {
        log.info("Received Kafka message: key=" + key + ", payload=" + payload);
        try {
            inventoryEventService.consumeOrderCreated(key, payload);
            log.info("Kafka message processed successfully for key={}", key);
        } catch (Exception e){
            log.error("Failed to process Kafka message for key={}", key, e);
        }
    }
}
