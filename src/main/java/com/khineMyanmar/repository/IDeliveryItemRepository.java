package com.khineMyanmar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khineMyanmar.model.DeliveryItem;

public interface IDeliveryItemRepository extends JpaRepository<DeliveryItem,Long> {
    List<DeliveryItem> findByDeliveryPersonUserId(Long deliveryPersonId);
}
