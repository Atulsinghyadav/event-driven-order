package com.eventdriven.orderservice.api;


import com.eventdriven.orderservice.domain.Order;
import com.eventdriven.orderservice.service.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Map<String, String> createOrder(@RequestBody CreateOrderRequest request){

        Order savedOrder = orderService.createOrder(request);

        return Map.of("orderId", savedOrder.getId().toString());
    }
}
