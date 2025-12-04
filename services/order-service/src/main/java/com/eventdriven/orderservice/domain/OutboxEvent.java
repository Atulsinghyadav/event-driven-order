package com.eventdriven.orderservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_events")
@NoArgsConstructor
@Getter
@Setter
public class OutboxEvent {

    @Id
    private UUID id;

    private String aggregateType;

    private UUID aggregateId;

    private String type;

    private String payload;

    private String status;

    private Instant createdAt;


    public void PrePersist(){

        if(id == null){
            id = UUID.randomUUID();
        }

        if(status == null){
            status = "PENDING";
        }

        if(createdAt == null){
            createdAt = Instant.now();
        }
    }

    public static OutboxEvent of(UUID aggregateId, String type, String payload){

        OutboxEvent event = new OutboxEvent();

        event.aggregateId = aggregateId;
        event.type = type;
        event.aggregateType = "Order";
        event.payload = payload;

        return event;

    }

}
