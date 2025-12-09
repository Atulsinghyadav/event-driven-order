package com.eventdriven.orderservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor

public class Order {

    @Id
    private UUID id;

    private String customerId;

    private BigDecimal totalAmount;

    private String currency;

    private String status;

    private Instant createdAt;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private List<OrderItem> items;

    @PrePersist
    public void prePersist(){

        if(id == null) {
            id = UUID.randomUUID();
        }

        createdAt = Instant.now();

        if(status == null){
            status = "PENDING";
        }
    }
}
