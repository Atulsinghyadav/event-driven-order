package com.eventdriven.orderservice.service;

import com.eventdriven.orderservice.api.CreateOrderRequest;
import com.eventdriven.orderservice.repository.OrderRepository;
import com.eventdriven.orderservice.repository.OutboxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.aspectj.weaver.ast.Or;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderService{

  private OrderRepository orderRepository;
  private OutboxRepository outboxRepository;

  private ObjectMapper mapper = new ObjectMapper();

  public OrderService(OrderRepository orderRepository, OutboxRepository outboxRepository){
      this.orderRepository = orderRepository;
      this.outboxRepository = outboxRepository;
  }

  @Transactional
  public Order createOrder(CreateOrderRequest request) throws IllegalArgumentException{

      try{
          if(request != null && request.getItems() != null && !request.getItems().isEmpty() && request.getCustomerId() != null){


          }
      }


  catch (Exception e) {
          throw new RuntimeException(e);
      }

      return ;
  }}
