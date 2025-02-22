package com.khineMyanmar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khineMyanmar.model.Order;

public interface IOrderRepository extends JpaRepository<Order,Long>{
    
}
