package com.eventdriven.orderservice.dto;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class Item {
    @NotBlank
    private String sku;
    @NotNull
    @Min(1)
    private Integer quantity;
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal unitPrice;

}
