package org.example.delivery_order.dtos;

import lombok.Data;
import org.example.delivery_order.models.Order;
@Data
public class OrderResponse {
    private Order order;
    private ResponseStatus status;
}
