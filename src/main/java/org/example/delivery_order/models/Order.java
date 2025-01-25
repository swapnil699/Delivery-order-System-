package org.example.delivery_order.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "orders")
public class Order extends BaseModel {
    private String name;
    private String address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    private LocalDateTime timestamp;

    public Order() {
    }
}
