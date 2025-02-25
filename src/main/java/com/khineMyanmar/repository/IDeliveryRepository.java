package com.khineMyanmar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khineMyanmar.model.Delivery;
import com.khineMyanmar.model.Shop;
import com.khineMyanmar.model.WorkingStatus;

import java.util.List;
import java.util.Optional;


public interface IDeliveryRepository extends JpaRepository<Delivery, Long> {
    public Optional<Delivery> findByUserId(long userId);
    public List<Delivery> findByShop(Shop shop);
    List<Delivery> findByShop_ShopIdAndWorkingStatusIn(Long shopId, List<WorkingStatus> statuses);
}
