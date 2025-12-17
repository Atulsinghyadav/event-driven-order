package com.eventdriven.orderservice.domain;

import com.eventdriven.orderservice.dto.Item;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.kafka.common.protocol.types.Field;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_items")
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
    public void PrePersist(){

        if(id == null){
            id = UUID.randomUUID();
        }
    }

    public static OrderItem from(Item dto){
        OrderItem it = new OrderItem();

        it.setSku(dto.getSku());
        it.setQuantity(dto.getQuantity());
        it.setUnitPrice(dto.getUnitPrice());

        return it;
    }

}
