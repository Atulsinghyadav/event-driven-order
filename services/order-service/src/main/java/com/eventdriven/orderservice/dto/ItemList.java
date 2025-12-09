package com.eventdriven.orderservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
public class ItemList {
    private String sku;
    private Integer quantity;
    private BigDecimal unitPrice;
}
