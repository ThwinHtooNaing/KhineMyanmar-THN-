package com.khineMyanmar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.khineMyanmar.model.ProductShop;
import com.khineMyanmar.model.Shop;

@Repository
public interface IProductShopRepository extends JpaRepository<ProductShop,Long>{
    boolean existsByShopAndProduct_ProductName(Shop shop, String productName);
}
