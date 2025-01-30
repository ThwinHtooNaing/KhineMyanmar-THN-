package com.khineMyanmar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khineMyanmar.model.Delivery;

public interface IDeliveryRepository extends JpaRepository<Delivery, Long> {
    
}
