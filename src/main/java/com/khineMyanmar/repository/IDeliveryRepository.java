package com.khineMyanmar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khineMyanmar.model.Delivery;
import java.util.Optional;


public interface IDeliveryRepository extends JpaRepository<Delivery, Long> {
    public Optional<Delivery> findByUserId(long userId);
}
