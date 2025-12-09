package com.eventdriven.orderservice.service;

import com.eventdriven.orderservice.api.CreateOrderRequest;
import com.eventdriven.orderservice.domain.Order;
import com.eventdriven.orderservice.domain.OrderItem;
import com.eventdriven.orderservice.domain.OutboxEvent;
import com.eventdriven.orderservice.repository.OrderRepository;
import com.eventdriven.orderservice.repository.OutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper mapper;
    private final Logger log = LoggerFactory.getLogger(OrderService.class);

    public OrderService(OrderRepository orderRepository, OutboxRepository outboxRepository) {
        this.orderRepository = orderRepository;
        this.outboxRepository = outboxRepository;
        this.mapper = new ObjectMapper();
    }

    @Transactional
    public Order createOrder(CreateOrderRequest req) {
        validateRequest(req);

        // map DTO -> Order
        Order order = new Order();
        order.setCustomerId(req.getCustomerId());
        order.setCurrency(req.getCurrency());
        order.setTotalAmount(req.getTotalAmount());

        List<OrderItem> items = req.getItems().stream().map(i -> {
            OrderItem it = new OrderItem();
            it.getItems().setSku(i.getSku());
            it.getItems().setQuantity(i.getQuantity());
            it.getItems().setUnitPrice(i.getUnitPrice());
            return it;
        }).collect(Collectors.toList());

        order.setItems(items);

        // save order (prePersist will set id + createdAt)
        Order saved = orderRepository.save(order);
        log.info("Saved order id={} items={}", saved.getId(), saved.getItems() == null ? 0 : saved.getItems().size());

        // create payload
        String payloadJson = buildOutboxPayload(saved);

        // create and save outbox event
        OutboxEvent ev = OutboxEvent.of(saved.getId(), "OrderCreated", payloadJson);
        outboxRepository.save(ev);

        log.info("Outbox event {} saved for order {}", ev.getId(), saved.getId());
        return saved;
    }

    private void validateRequest(CreateOrderRequest req) {
        if (req == null) throw new IllegalArgumentException("Request cannot be null");
        if (req.getCustomerId() == null || req.getCustomerId().isBlank()) throw new IllegalArgumentException("customerId required");
        if (req.getItems() == null || req.getItems().isEmpty()) throw new IllegalArgumentException("items required");
        if (req.getTotalAmount() == null) throw new IllegalArgumentException("totalAmount required");
    }

    private String buildOutboxPayload(Order saved) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("orderId", saved.getId() == null ? null : saved.getId().toString());
        payload.put("customerId", saved.getCustomerId());
        payload.put("totalAmount", saved.getTotalAmount());
        payload.put("currency", saved.getCurrency());
        payload.put("items", Optional.ofNullable(saved.getItems()).orElse(Collections.emptyList()).stream().map(it -> {
            Map<String, Object> m = new HashMap<>();
            m.put("sku", it.getItems().getSku());
            m.put("quantity", it.getItems().getQuantity());
            m.put("unitPrice", it.getItems().getUnitPrice());
            return m;
        }).collect(Collectors.toList()));

        try {
            return mapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize outbox payload for order {}", saved.getId(), e);
            throw new RuntimeException("Failed to serialize outbox payload", e);
        }
    }
}