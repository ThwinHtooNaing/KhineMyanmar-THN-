package com.khineMyanmar.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.khineMyanmar.model.Order;
import com.khineMyanmar.model.Product;
import com.khineMyanmar.model.User;
import com.khineMyanmar.model.OrderProduct;
import com.khineMyanmar.model.OrderStatus;
import com.khineMyanmar.repository.IOrderProductRepository;
import com.khineMyanmar.repository.IOrderRepository;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
        order.setCheckoutDate(LocalDateTime.now()); 
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
}