package com.khineMyanmar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.khineMyanmar.DTO.TopSaleProductDTO;
import com.khineMyanmar.model.OrderProduct;

public interface IOrderProductRepository extends JpaRepository<OrderProduct,Long>{

    @Query("SELECT new com.khineMyanmar.DTO.TopSaleProductDTO(p.productName, p.productImagePath, ps.productPrice, SUM(op.quantity)) " +
       "FROM OrderProduct op JOIN op.product p JOIN ProductShop ps ON ps.product.productId = p.productId " +
       "GROUP BY p.productId, p.productName, p.productImagePath, ps.productPrice " +
       "ORDER BY SUM(op.quantity) DESC")
    List<TopSaleProductDTO> findTopSellingProducts();

    @Query("SELECT new com.khineMyanmar.DTO.TopSaleProductDTO(p.productName, p.productImagePath, ps.productPrice, SUM(op.quantity)) " +
       "FROM OrderProduct op " +
       "JOIN op.product p " +
       "JOIN ProductShop ps ON ps.product.productId = p.productId " +
       "WHERE ps.shop.shopId = :shopId " + // Filter by shopId
       "GROUP BY p.productId, p.productName, p.productImagePath, ps.productPrice " +
       "ORDER BY SUM(op.quantity) DESC")
    List<TopSaleProductDTO> findTopSellingProductsByShop(@Param("shopId") Long shopId);

    default List<TopSaleProductDTO> findTopFourSellingProductsByShop(Long shopId) {
        return findTopSellingProductsByShop(shopId).stream().limit(4).toList();
    }

    // New method to get top 4 selling products
    default List<TopSaleProductDTO> findTopFourSellingProducts() {
        return findTopSellingProducts().stream().limit(4).toList();
    }

    default List<TopSaleProductDTO> findTopSevenSellingProducts() {
        return findTopSellingProducts().stream().limit(7).toList();
    }

    @Query("SELECT p.category.categoryName, SUM(op.quantity) FROM OrderProduct op JOIN op.product p GROUP BY p.category.categoryName")
    List<Object[]> getSalesByCategory();
}
