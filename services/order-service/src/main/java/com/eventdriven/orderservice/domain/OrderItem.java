package com.eventdriven.orderservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "orderItem")
@NoArgsConstructor
@Getter
@Setter
public class OrderItem {


    @Id
    private UUID id;

    private String sku;

    private Integer quantity;

    private BigDecimal unitPrice;

    public void PrePersist(){

        if(id == null){
            id = UUID.randomUUID();
        }
    }

}
