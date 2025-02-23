package com.khineMyanmar.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.khineMyanmar.model.Order;
import com.khineMyanmar.model.OrderStatus;

public interface IOrderRepository extends JpaRepository<Order,Long>{
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    @Query("SELECT COUNT(o) FROM Order o")
    long countTotalOrders();

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = 'DELIVERED'")
    long countDeliveredOrders();

    @Query("SELECT COUNT(DISTINCT o.user) FROM Order o")
    Long countTotalCustomers();

    @Query("SELECT COALESCE(SUM(o.amount), 0) FROM Order o")
    double totalEarnings();

    @Query("SELECT COUNT(o) FROM Order o JOIN o.orderProducts op JOIN op.product p JOIN ProductShop ps ON ps.product.productId = p.productId WHERE ps.shop.shopId = :shopId")
    long countTotalOrdersByShop(@Param("shopId") Long shopId);

    @Query("SELECT COUNT(o) FROM Order o JOIN o.orderProducts op JOIN op.product p JOIN ProductShop ps ON ps.product.productId = p.productId WHERE ps.shop.shopId = :shopId AND o.status = 'DELIVERED'")
    long countDeliveredOrdersByShop(@Param("shopId") Long shopId);

    @Query("SELECT COALESCE(SUM(o.amount), 0) FROM Order o JOIN o.orderProducts op JOIN op.product p JOIN ProductShop ps ON ps.product.productId = p.productId WHERE ps.shop.shopId = :shopId")
    double totalEarningsByShop(@Param("shopId") Long shopId);

    @Query("SELECT o FROM Order o JOIN o.orderProducts op JOIN op.product p JOIN ProductShop ps ON ps.product.productId = p.productId " +
           "WHERE o.status = :status AND ps.shop.shopId = :shopId")
    Page<Order> findByStatusAndShop(@Param("status") OrderStatus status, @Param("shopId") Long shopId, Pageable pageable);

    @Query("SELECT COUNT(o), o.checkoutDate FROM Order o WHERE o.checkoutDate >= :startDate GROUP BY o.checkoutDate")
    List<Object[]> countOrdersLastWeek(@Param("startDate") LocalDate startDate);

    @Query("SELECT COUNT(o), o.checkoutDate FROM Order o " +
           "JOIN o.orderProducts op " +
           "JOIN op.product p " +
           "JOIN ProductShop ps ON ps.product.productId = p.productId " +
           "WHERE ps.shop.shopId = :shopId AND o.checkoutDate >= :startDate " +
           "GROUP BY o.checkoutDate")
    List<Object[]> countOrdersLastWeek(@Param("shopId") Long shopId, @Param("startDate") LocalDate startDate);

     // Fetch the latest 5 orders based on orderId (higher orderId means latest)
    List<Order> findTop5ByOrderByOrderIdDesc();

    List<Order> findTop6ByOrderByOrderIdDesc();

     @Query(value = "SELECT o.* FROM `order` o " +
                   "JOIN order_product op ON o.order_id = op.order_id " +
                   "JOIN product p ON op.product_id = p.product_id " +
                   "JOIN product_shop ps ON ps.product_id = p.product_id " +
                   "WHERE ps.shop_id = :shopId " +
                   "ORDER BY o.order_id DESC " +
                   "LIMIT 5", nativeQuery = true)
    List<Order> findTop5ByShopIdOrderByOrderIdDesc(@Param("shopId") Long shopId);

    @Query("SELECT o.user.userId, o.user.firstName, o.user.lastName, o.user.profilePic, SUM(o.amount) " +
           "FROM Order o GROUP BY o.user.userId, o.user.firstName, o.user.lastName, o.user.profilePic " +
           "ORDER BY SUM(o.amount) DESC")
    List<Object[]> findTopCustomers();

    @Query("SELECT o.user.userId, o.user.firstName, o.user.lastName, o.user.profilePic, SUM(o.amount) " +
           "FROM Order o " +
           "JOIN o.orderProducts op " +
           "JOIN op.product p " +
           "JOIN ProductShop ps ON ps.product.productId = p.productId " +
           "WHERE ps.shop.shopId = :shopId " +
           "GROUP BY o.user.userId, o.user.firstName, o.user.lastName, o.user.profilePic " +
           "ORDER BY SUM(o.amount) DESC")
    List<Object[]> findTopCustomersByShop(@Param("shopId") Long shopId);

    @Query("SELECT o FROM Order o JOIN o.orderProducts op JOIN op.product p JOIN ProductShop ps ON ps.product.productId = p.productId WHERE ps.shop.shopId = :shopId")
    List<Order> findOrdersByShop(@Param("shopId") Long shopId);

    @Query("SELECT o FROM Order o JOIN o.orderProducts op JOIN op.product p JOIN ProductShop ps ON ps.product.productId = p.productId WHERE ps.shop.shopId = :shopId")
    Page<Order> findOrdersByShop(@Param("shopId") Long shopId, Pageable pageable);

    @Query("SELECT MONTH(o.checkoutDate) AS month, SUM(o.amount) AS totalSales " +
           "FROM Order o " +
           "GROUP BY MONTH(o.checkoutDate) " +
           "ORDER BY month ASC")
    List<Object[]> getMonthlySalesData();

    
}
