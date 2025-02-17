package com.khineMyanmar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.khineMyanmar.model.Shop;
@Repository
public interface IShopRepository extends JpaRepository<Shop,Long> {
    // Fetch the top 6 shops, sorted by shopId (ascending)
    List<Shop> findTop6ByOrderByShopIdAsc();
}
