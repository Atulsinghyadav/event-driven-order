package com.eventdriven.inventoryservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "processed_events")
@Getter
@Setter
@NoArgsConstructor
public class ProcessedEvent {

    @Id
    private String eventKey;

    @Column(name = "consumer_name")
    private String consumerName;

    private Instant processedAt;

    @PrePersist
    public void PrePersist(){
            if(processedAt == null){
                processedAt = Instant.now();
            }
    }
}
