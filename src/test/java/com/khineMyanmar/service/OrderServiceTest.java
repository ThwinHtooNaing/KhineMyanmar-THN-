package com.khineMyanmar.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
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
        Long shopId = 1L;
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        
        // Correct mock data: [Order count, Date]
        List<Object[]> results = List.of(
            new Object[]{10L, yesterday},
            new Object[]{20L, today}
        );

        when(orderRepository.countOrdersLastWeek(eq(shopId), any(LocalDate.class))).thenReturn(results);

        Map<String, Object> stats = orderService.getWeeklyOrderStats(shopId);
        
        assertNotNull(stats);
        assertTrue(stats.containsKey("labels"));
        assertTrue(stats.containsKey("orderCounts"));

        List<Integer> orderCounts = (List<Integer>) stats.get("orderCounts");
        assertNotNull(orderCounts);
        assertEquals(7, orderCounts.size()); // Should have 7 values for the 7 days

        // Ensure the correct order count is present for today and yesterday
        assertEquals(10, orderCounts.get(5)); // 6 days ago to today (index 5 corresponds to yesterday)
        assertEquals(20, orderCounts.get(6)); // Today (index 6)
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
        // Mock user session
        when(session.getAttribute("customerSession")).thenReturn(user);

        // Mock cart session
        List<Map<String, Object>> cart = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("productId", 1L);
        item.put("quantity", 2);
        cart.add(item);
        when(session.getAttribute("cart")).thenReturn(cart);

        // Mock product service
        when(productService.getProductPrice(1L)).thenReturn(50.0);
        Product mockProduct = new Product();
        mockProduct.setProductId(1L);
        when(productService.getProductById(1L)).thenReturn(mockProduct);

        // Mock order repository
        Order mockOrder = new Order();
        mockOrder.setOrderId(100L); // Ensure order has an ID
        mockOrder.setOrderProducts(new HashSet<>()); // Avoid null list issue
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);

        // Mock order product repository
        OrderProduct mockOrderProduct = new OrderProduct();
        when(orderProductRepository.save(any(OrderProduct.class))).thenReturn(mockOrderProduct);

        // Mock stock reduction
        doNothing().when(productShopService).reduceStockQuantity(any(Product.class), anyInt());

        // Call checkout
        String result = orderService.checkout(session);

        // Validate response
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
