package com.eventdriven.orderservice.domain;

import com.eventdriven.orderservice.dto.ItemList;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "orderItem")
@NoArgsConstructor
@Getter
@Setter
public class OrderItem {


    @Id
    private UUID id;

    private ItemList items;

    public void PrePersist(){

        if(id == null){
            id = UUID.randomUUID();
        }
    }

}
