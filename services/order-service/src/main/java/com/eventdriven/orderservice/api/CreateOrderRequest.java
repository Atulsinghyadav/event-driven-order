package com.eventdriven.orderservice.api;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.kafka.common.protocol.types.Field;

import java.math.BigDecimal;
import java.util.List;


@NoArgsConstructor
@Getter
@Setter
public class CreateOrderRequest {

    private String customerId;
    private String currency;
    private BigDecimal totalAmount;
    private List<Item> items;

    public class Item {

        private String sku;
        private Integer quantity;
        private BigDecimal unitPrice;
    }
}
