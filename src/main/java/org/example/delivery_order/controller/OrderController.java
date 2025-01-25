package org.example.delivery_order.controller;

import org.example.delivery_order.dtos.OrderDTO;
import org.example.delivery_order.dtos.OrderResponse;
import org.example.delivery_order.dtos.ResponseStatus;
import org.example.delivery_order.exceptions.InvalidNameException;
import org.example.delivery_order.models.DeliveryStatus;
import org.example.delivery_order.models.Order;
import org.example.delivery_order.service.OrderService;
import org.hibernate.query.Page;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // http://localhost:8080/api/v1/orders/add
    @PostMapping("/add")
    public OrderResponse addorders(@RequestBody OrderDTO orderDTO) {
        OrderResponse response = new OrderResponse();
        try {
            Order order = orderService.addOrder(orderDTO.getCustomerName(), orderDTO.getAddress());
            response.setOrder(order);
            response.setStatus(ResponseStatus.SUCCESS);
        } catch (InvalidNameException e) {
            response.setStatus(ResponseStatus.FAILURE);
        }
        return response;
    }

    // http://localhost:8080/api/v1/orders?status=PENDING
    @GetMapping
    public List<Order> getOrdersByStatus(@RequestParam DeliveryStatus status) {

        List<Order> orders = orderService.getOrdersByStatus(status);
        return orders;
    }
    //pagination of all order list

//    @GetMapping("/pagination/{page}/{pagesize}")
//    public Page<Order> getOrdersByStatus(@PathVariable int page, @PathVariable int pagesize) {
//        return (Page<Order>) orderService.getProductWithPagination(page, pagesize);
//    }



    // http://localhost:8080/api/v1/orders/top
    @GetMapping("/top")
    public List<String> getTop3Customers() {
        return orderService.getTop3Customers();
    }
    // http://localhost:8080/api/v1/orders/simulate
    @GetMapping("/simulate")
    public String simulateOrderProcessing() {
        orderService.simulateOrderProcessing();
        return "Order processing simulation started.";
    }
}
