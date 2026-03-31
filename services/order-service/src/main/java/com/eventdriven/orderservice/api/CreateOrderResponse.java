package com.eventdriven.orderservice.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class CreateOrderResponse {

    private UUID orderId;
    private String status;
    private Instant createdAt;

}
