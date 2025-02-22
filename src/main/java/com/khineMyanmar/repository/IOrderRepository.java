package com.khineMyanmar.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.khineMyanmar.model.Order;
import com.khineMyanmar.model.OrderStatus;

public interface IOrderRepository extends JpaRepository<Order,Long>{
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    @Query("SELECT COUNT(o) FROM Order o")
    long countTotalOrders();

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = 'DELIVERED'")
    long countDeliveredOrders();

    @Query("SELECT COALESCE(SUM(o.amount), 0) FROM Order o")
    double totalEarnings();

    @Query("SELECT COUNT(o), o.checkoutDate FROM Order o WHERE o.checkoutDate >= :startDate GROUP BY o.checkoutDate")
    List<Object[]> countOrdersLastWeek(@Param("startDate") LocalDate startDate);
}
