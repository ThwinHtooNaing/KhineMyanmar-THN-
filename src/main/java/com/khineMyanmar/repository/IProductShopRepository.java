package com.khineMyanmar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    Page<ProductShop> findByShop(Shop shop, Pageable pageable);
    Optional<ProductShop> findStockQuantityByProduct(Product product);
    Optional<ProductShop> findProductPriceByProduct(Product product);
    
    @Query("SELECT ps FROM ProductShop ps WHERE ps.product.productId = :productId")
    Optional<ProductShop> findProductPriceByProductId(@Param("productId") Long productId);

    @Modifying
    @Query("UPDATE ProductShop ps SET ps.stockQuantity = ps.stockQuantity - :quantity WHERE ps.product = :product AND ps.stockQuantity >= :quantity")
    int reduceStockQuantity(@Param("product") Product product, @Param("quantity") int quantity);

}
