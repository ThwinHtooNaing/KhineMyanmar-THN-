package com.khineMyanmar.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.khineMyanmar.model.Product;

@Repository
public interface IProductRepository extends JpaRepository<Product,Long> {
    Page<Product> findByCategory_CategoryName(String categoryName, Pageable pageable);
    Page<Product> findByProductShops_Shop_ShopName(String shopName, Pageable pageable);

}
