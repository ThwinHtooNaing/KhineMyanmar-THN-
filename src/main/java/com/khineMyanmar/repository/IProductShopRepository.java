package com.khineMyanmar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.khineMyanmar.model.Product;
import com.khineMyanmar.model.ProductShop;
import com.khineMyanmar.model.Shop;

@Repository
public interface IProductShopRepository extends JpaRepository<ProductShop,Long>{
    boolean existsByShopAndProduct_ProductName(Shop shop, String productName);
    List<ProductShop> findByShop(Shop shop);
    Optional<ProductShop> findByProductAndShop(Product product,Shop shop);
    Optional<ProductShop> findByProduct(Product product);
}
