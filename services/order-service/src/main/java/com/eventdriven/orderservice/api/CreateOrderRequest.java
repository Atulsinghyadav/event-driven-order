package com.eventdriven.orderservice.api;

import com.eventdriven.orderservice.dto.ItemList;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;


@NoArgsConstructor
@Getter
@Setter
public class CreateOrderRequest {

    private String customerId;
    private String currency;
    private BigDecimal totalAmount;
    private List<ItemList> items;

}
