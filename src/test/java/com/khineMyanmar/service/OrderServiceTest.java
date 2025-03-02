package com.khineMyanmar.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.khineMyanmar.DTO.TopSaleProductDTO;
import com.khineMyanmar.model.*;
import com.khineMyanmar.repository.IOrderProductRepository;
import com.khineMyanmar.repository.IOrderRepository;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private IOrderRepository orderRepository;

    @Mock
    private IOrderProductRepository orderProductRepository;

    @Mock
    private ProductService productService;

    @Mock
    private ProductShopService productShopService;

    @Mock
    private HttpSession session;

    private Order order;
    private User user;
    private List<Order> orderList;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1L);
        
        order = new Order();
        order.setOrderId(1L);
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setAmount(100.0);
        orderList = List.of(order);
    }

    @SuppressWarnings("unchecked")
    @Test
    void testGetWeeklyOrderStats() {
        // Ensure List<Object[]> is used
        List<Object[]> results;
        results = List.of(new Object[]{LocalDate.now().minusDays(1), 10}, new Object[]{LocalDate.now(), 20});
        when(orderRepository.countOrdersLastWeek(any(LocalDate.class))).thenReturn(results);

        Map<String, Object> stats = orderService.getWeeklyOrderStats();
        assertNotNull(stats);
        assertTrue(stats.containsKey("labels"));
        assertTrue(stats.containsKey("orderCounts"));

        
        List<Integer> orderCounts = (List<Integer>) stats.get("orderCounts");
        assertNotNull(orderCounts);
        assertFalse(orderCounts.isEmpty());
        assertEquals(1, orderCounts.size());
        assertEquals(10, orderCounts.get(0));
    }

    @Test
    void testGetDashboardStats() {
        when(orderRepository.countTotalOrders()).thenReturn(10L);
        when(orderRepository.totalEarnings()).thenReturn(500.0);
        when(orderRepository.countTotalCustomers()).thenReturn(5L);
        
        Map<String, Object> stats = orderService.getDashboardStats();
        assertEquals(10L, stats.get("totalOrders"));
        assertEquals(500.0, stats.get("totalEarnings"));
        assertEquals(5L, stats.get("totalCustomers"));
    }

    @Test
    void testCheckout() {
        when(session.getAttribute("customerSession")).thenReturn(user);
        List<Map<String, Object>> cart = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("productId", 1L);
        item.put("quantity", 2);
        cart.add(item);
        when(session.getAttribute("cart")).thenReturn(cart);
        when(productService.getProductPrice(1L)).thenReturn(50.0);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        
        String result = orderService.checkout(session);
        assertEquals("Order placed successfully", result);
    }

    @Test
    void testFindAllOrders() {
        when(orderRepository.findAll()).thenReturn(orderList);
        List<Order> result = orderService.findAllOrders();
        assertEquals(1, result.size());
        assertEquals(order, result.get(0));
    }

    @Test
    void testGetOrders() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Order> page = new PageImpl<>(orderList);
        when(orderRepository.findAll(pageable)).thenReturn(page);
        
        Page<Order> result = orderService.getOrders(null, pageable);
        assertEquals(1, result.getTotalElements());
    }
    @Test
    void testGetSalesByCategory() {
        // Ensure List<Object[]> is used
        List<Object[]> results;
        results = List.of(new Object[]{"Electronics", 50L},new Object[]{"Computer", 50L});
        when(orderProductRepository.getSalesByCategory()).thenReturn(results);

        Map<String, Long> salesData = orderService.getSalesByCategory();
        assertNotNull(salesData);
        assertTrue(salesData.containsKey("Electronics"));
        assertEquals(50L, salesData.get("Electronics"));
    }


    @Test
    void testFindTopSevenSellingProducts() {
        List<TopSaleProductDTO> topProducts = List.of(new TopSaleProductDTO(1L, "Laptop", 10D));
        when(orderProductRepository.findTopSevenSellingProducts()).thenReturn(topProducts);
        
        List<TopSaleProductDTO> result = orderService.findTopSevenSellingProducts();
        assertEquals(1, result.size());
        assertEquals("Laptop", result.get(0).getProductName());
    }
}
