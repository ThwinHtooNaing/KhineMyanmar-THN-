package com.khineMyanmar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.khineMyanmar.model.Shop;
@Repository
public interface IShopRepository extends JpaRepository<Shop,Long> {

}
