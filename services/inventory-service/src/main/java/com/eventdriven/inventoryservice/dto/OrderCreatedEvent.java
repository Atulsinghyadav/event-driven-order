package com.eventdriven.inventoryservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderCreatedEvent {

    private String orderId;
    private String customerId;
    private BigDecimal totalAmount;
    private String currency;
    private List<OrderItemPayLoad> items;

    @NoArgsConstructor
    @Getter
    @Setter
    public static class OrderItemPayLoad{

        private String sku;
        private Integer quantity;
        private BigDecimal unitPrice;
    }

}
