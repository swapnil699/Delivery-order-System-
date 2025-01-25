package org.example.delivery_order.service;

import org.example.delivery_order.exceptions.InvalidNameException;
import org.example.delivery_order.models.DeliveryStatus;
import org.example.delivery_order.models.Order;
import org.springframework.data.domain.Page;

import java.util.List;


public interface OrderService {
    Order addOrder(String customerName, String address) throws InvalidNameException;
    List<Order> getOrdersByStatus(DeliveryStatus status);
    List<String> getTop3Customers();
    void simulateOrderProcessing();

    Page<Order> getProductWithPagination(int offset, int pagesize);
}
