package org.example.delivery_order.repositorys;

import org.example.delivery_order.models.DeliveryStatus;
import org.example.delivery_order.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order>  findByDeliveryStatus(DeliveryStatus status);
    @Query("SELECT o.name AS customerName, COUNT(o.id) AS deliveredOrders " +
            "FROM Order o WHERE o.deliveryStatus = 'DELIVERED' " +
            "GROUP BY o.name " +
            "ORDER BY deliveredOrders DESC")
    List<Object[]> findTop3CustomersByDeliveredOrders();
}
