package com.khineMyanmar.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.khineMyanmar.model.Product;

@Repository
public interface IProductRepository extends JpaRepository<Product,Long> {

    Page<Product> findByCategory_CategoryName(String categoryName, Pageable pageable);
    Page<Product> findByProductShops_Shop_ShopName(String shopName, Pageable pageable);
    void deleteByProductIdIn(List<Long> productIds);
    List<Product> findTop8ByOrderByProductNameAsc();

   @Query("SELECT p FROM Product p JOIN p.productShops ps WHERE ps.shop.shopId = :shopId")
    List<Product> findProductsByShopId(@Param("shopId") Long shopId);


}
