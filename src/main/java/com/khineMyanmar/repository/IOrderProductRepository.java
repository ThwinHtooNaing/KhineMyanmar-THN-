package com.khineMyanmar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khineMyanmar.model.OrderProduct;

public interface IOrderProductRepository extends JpaRepository<OrderProduct,Long>{
    
}
