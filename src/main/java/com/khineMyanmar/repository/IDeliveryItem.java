package com.khineMyanmar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khineMyanmar.model.DeliveryItem;

public interface IDeliveryItem extends JpaRepository<DeliveryItem,Long> {
    
}
