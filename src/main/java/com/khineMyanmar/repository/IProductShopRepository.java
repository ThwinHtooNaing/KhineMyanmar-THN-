package com.khineMyanmar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.khineMyanmar.model.ProductShop;

@Repository
public interface IProductShopRepository extends JpaRepository<ProductShop,Long>{
    
}
