package com.eventdriven.inventoryservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Primary;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "inventory_reservations")
@Getter
@Setter
@NoArgsConstructor
public class InventoryReservation {

    @Id
    private UUID id;

    @Column(name = "order_id")
    private UUID orderId;

    private String sku;

    private Integer quantity;

    private String status;

    private Instant createdAt;

    @PrePersist
    public void prePersist(){
        if(createdAt == null){
            createdAt = Instant.now();
        }

        if(id == null){
            id = UUID.randomUUID();
        }

        if(status == null){
            status = "RESERVED";
        }
    }


}
