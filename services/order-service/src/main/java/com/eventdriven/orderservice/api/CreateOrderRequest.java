package com.eventdriven.orderservice.api;

//import com.eventdriven.orderservice.domain.OrderItem;
import com.eventdriven.orderservice.dto.Item;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;


@NoArgsConstructor
@Getter
@Setter
public class CreateOrderRequest {


    @NotBlank
    private String customerId;
    @NotBlank
    private String currency;
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal totalAmount;
    @NotEmpty
    @Valid
    private List<Item> items;


}
