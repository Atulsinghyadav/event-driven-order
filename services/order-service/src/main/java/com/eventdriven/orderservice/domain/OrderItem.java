package com.eventdriven.orderservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_item")
@NoArgsConstructor
@Getter
@Setter
public class OrderItem {

    @Id
    private UUID id;

    private String sku;
    
    private Integer quantity;
    
    private BigDecimal unitPrice;

    @PrePersist
    public void prePersist(){
        if(id == null){
            id = UUID.randomUUID();
        }
    }

}
