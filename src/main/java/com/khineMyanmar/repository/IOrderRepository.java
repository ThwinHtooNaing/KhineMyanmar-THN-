package com.khineMyanmar.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.khineMyanmar.model.Order;
import com.khineMyanmar.model.OrderStatus;

public interface IOrderRepository extends JpaRepository<Order,Long>{
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
}
