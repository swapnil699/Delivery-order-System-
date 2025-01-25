package org.example.delivery_order.service;

import org.example.delivery_order.exceptions.InvalidNameException;
import org.example.delivery_order.models.DeliveryStatus;
import org.example.delivery_order.models.Order;
import org.example.delivery_order.repositorys.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order addOrder(String customerName, String address) throws InvalidNameException {
        // Validate customer name
        if (customerName == null || customerName.length() < 2) {
            throw new InvalidNameException("Invalid Name: Name must have at least 2 characters.");
        }

        Order newOrder = new Order();
        newOrder.setName(customerName);
        newOrder.setAddress(address);
        newOrder.setDeliveryStatus(DeliveryStatus.PENDING);
        newOrder.setTimestamp(LocalDateTime.now());

        // Save order to repository
        return orderRepository.save(newOrder);
    }
    // list of all orders
    @Override
    public List<Order> getOrdersByStatus(DeliveryStatus status) {
        List<Order> alllist = orderRepository.findByDeliveryStatus(status);
        if(alllist.isEmpty()){
            throw new RuntimeException("not recivied order! please add order");
        }
        return alllist;
    }
    // pagination of all order list

    @Override
    public List<String> getTop3Customers() {
        List<Object[]> results = orderRepository.findTop3CustomersByDeliveredOrders();
        return results.stream()
                .map(row -> "customer: " + row[0] + ", Delivered Orders: " + row[1])
                .collect(Collectors.toList());
    }

    @Override
    public void simulateOrderProcessing() {
        ExecutorService executorService = Executors.newFixedThreadPool(5); // Simulate 5 delivery agents

        for (int i = 0; i < 5; i++) {
            executorService.submit(() -> {
                while (true) {
                    synchronized (this) {
                        List<Order> pendingOrderOpt = orderRepository.findByDeliveryStatus(DeliveryStatus.PENDING);

                        if (pendingOrderOpt.isEmpty()) {
                            break; // No more pending orders khali
                        }
                        int index = 0;
                        while (pendingOrderOpt.size() != index){
                            Order order = pendingOrderOpt.get(index);
                            order.setDeliveryStatus(DeliveryStatus.IN_PROGRESS);
                            orderRepository.save(order);
                        }
                    }

                    try {
                        Thread.sleep(3000); // 3 sec sleep time
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                    synchronized (this) {
                        List<Order> pendingOrderOpt = orderRepository.findByDeliveryStatus(DeliveryStatus.IN_PROGRESS);
                        int index = 0;
                        while (pendingOrderOpt.size() != index){
                            Order order = pendingOrderOpt.get(index);
                            order.setDeliveryStatus(DeliveryStatus.DELIVERED);
                            orderRepository.save(order);
                        }
                    }
                }
            });
        }

        executorService.shutdown();
    }

    @Override
    public Page<Order> getProductWithPagination(int page, int pageSize) {
        return orderRepository.findAll(PageRequest.of(page, pageSize));
    }

}
