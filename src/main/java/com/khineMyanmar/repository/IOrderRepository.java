package com.khineMyanmar.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.khineMyanmar.model.Order;
import com.khineMyanmar.model.OrderStatus;

public interface IOrderRepository extends JpaRepository<Order,Long>{
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    @Query("SELECT COUNT(o) FROM Order o")
    long countTotalOrders();

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = 'DELIVERED'")
    long countDeliveredOrders();

    @Query("SELECT COALESCE(SUM(o.amount), 0) FROM Order o WHERE o.paymentStatus = 'PAID'")
    double totalEarnings();
}
