package com.khineMyanmar.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.khineMyanmar.DTO.TopCustomerDTO;
import com.khineMyanmar.model.Order;
import com.khineMyanmar.model.Product;
import com.khineMyanmar.model.User;
import com.khineMyanmar.model.OrderProduct;
import com.khineMyanmar.model.OrderStatus;
import com.khineMyanmar.model.PaymentStatus;
import com.khineMyanmar.repository.IOrderProductRepository;
import com.khineMyanmar.repository.IOrderRepository;

import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IOrderProductRepository orderProductRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductShopService productShopService;

    public Map<String, Object> getWeeklyOrderStats(Long shopId) {
        LocalDate startDate = LocalDate.now().minusDays(6);
        List<Object[]> results = orderRepository.countOrdersLastWeek(shopId,startDate);

        Map<LocalDate, Integer> orderMap = results.stream()
            .collect(Collectors.toMap(r -> (LocalDate) r[1], r -> ((Long) r[0]).intValue()));

        List<String> labels = new ArrayList<>();
        List<Integer> orderCounts = new ArrayList<>();

        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            labels.add("Day " + (7 - i));
            orderCounts.add(orderMap.getOrDefault(date, 0));
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("labels", labels);
        stats.put("orderCounts", orderCounts);
        return stats;
    }

    public Map<String, Object> getWeeklyOrderStats() {
        LocalDate startDate = LocalDate.now().minusDays(6);
        List<Object[]> results = orderRepository.countOrdersLastWeek(startDate);

        Map<LocalDate, Integer> orderMap = results.stream()
            .collect(Collectors.toMap(r -> (LocalDate) r[1], r -> ((Long) r[0]).intValue()));

        List<String> labels = new ArrayList<>();
        List<Integer> orderCounts = new ArrayList<>();

        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            labels.add("Day " + (7 - i));
            orderCounts.add(orderMap.getOrDefault(date, 0));
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("labels", labels);
        stats.put("orderCounts", orderCounts);
        return stats;
    }

     public Map<String, Object> getDashboardStats(Long shopId) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalOrders", orderRepository.countTotalOrdersByShop(shopId));
        stats.put("deliveredOrders", orderRepository.countDeliveredOrdersByShop(shopId));
        stats.put("totalEarnings", orderRepository.totalEarningsByShop(shopId));
        return stats;
    }

    @Transactional
    public String checkout(HttpSession session) {
        // Retrieve user ID from session
        User user = (User) session.getAttribute("customerSession");
        if (user == null) {
            return "User  not logged in";
        }

        // Retrieve cart items from session
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> cart = (List<Map<String, Object>>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            return "Cart is empty";
        }

        Order order = new Order();
        order.setUser(user); 
        order.setStatus(OrderStatus.PENDING); 
        order.setPaymentStatus(PaymentStatus.CASH_ON_DELIVERY);
        order.setCheckoutDate(LocalDate.now()); 
        double totalAmount = 0.0;
        order.setAmount(0.0);
        order = orderRepository.save(order);
        for (Map<String, Object> item : cart) {
            Long productId = ((Number) item.get("productId")).longValue();
            int quantity = (Integer) item.get("quantity");
            double price = productService.getProductPrice(productId); 
            OrderProduct orderProduct = new OrderProduct();
            Product product = productService.getProductById(productId);
            productShopService.reduceStockQuantity(product, quantity);
            orderProduct.setOrder(order);
            orderProduct.setProduct(product); 
            orderProduct.setQuantity(quantity);
            orderProduct.setSoldPrice(price);
            orderProduct.setSubTotal(price * quantity);
            orderProduct = orderProductRepository.save(orderProduct);
            // Add to order
            order.getOrderProducts().add(orderProduct);
            totalAmount += orderProduct.getSubTotal();
        }

        order.setAmount(totalAmount); 
        orderRepository.save(order);
        session.removeAttribute("cart");

        return "Order placed successfully";
    }

    public Page<Order> getOrders(OrderStatus status, Pageable pageable) {
            if (status != null) {
                return orderRepository.findByStatus(status, pageable);
            }
        return orderRepository.findAll(pageable);
    }

    public Page<Order> getOrdersByStatusAndShop(OrderStatus status, Long shopId, Pageable pageable) {
        if (status != null && shopId != null) {
            return orderRepository.findByStatusAndShop(status, shopId, pageable);
        }
        return orderRepository.findOrdersByShop(shopId, pageable);
    }

    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Page<Order> getOrdersByShop(Long shopId, Pageable pageable) {
        return orderRepository.findOrdersByShop(shopId, pageable);
    }

    public List<Order> getRecentOrders() {
        return orderRepository.findTop5ByOrderByOrderIdDesc();
    }

    public List<TopCustomerDTO> getTopCustomers() {
        return orderRepository.findTopCustomers().stream()
            .limit(5) // Limit to top 5
            .map(obj -> new TopCustomerDTO(
                (Long) obj[0],  // userId
                (String) obj[1], // firstName
                (String) obj[2], // lastName
                (String) obj[3], // profilePic
                (Double) obj[4]  // totalAmount
            ))
            .collect(Collectors.toList());
    }

    public List<Order> getRecentOrdersByShop(Long shopId) {
        return orderRepository.findTop5ByShopIdOrderByOrderIdDesc(shopId);
    }

    public List<TopCustomerDTO> getTopCustomersByShop(Long shopId) {
        return orderRepository.findTopCustomersByShop(shopId).stream()
            .limit(5) // Limit to top 5
            .map(obj -> new TopCustomerDTO(
                (Long) obj[0],  // userId
                (String) obj[1], // firstName
                (String) obj[2], // lastName
                (String) obj[3], // profilePic
                (Double) obj[4]  // totalAmount
            ))
            .collect(Collectors.toList());
    }
}