package com.eventdriven.orderservice.dto;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class Item {
    private String sku;
    private Integer quantity;
    private BigDecimal unitPrice;

}
