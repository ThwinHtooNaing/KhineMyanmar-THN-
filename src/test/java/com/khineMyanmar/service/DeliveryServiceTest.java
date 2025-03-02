package com.khineMyanmar.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.khineMyanmar.model.*;
import com.khineMyanmar.repository.*;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceTest {

    @Mock
    private IDeliveryRepository deliveryRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private IUserRoleRepository roleRepository;

    @Mock
    private ShopServiceTest shopService;

    @Mock
    private StorageService storageService;

    @Mock
    private IOrderRepository orderRepository;

    @Mock
    private IDeliveryItemRepository deliveryItemRepository;

    @InjectMocks
    private DeliveryService deliveryService;

    private Delivery delivery;
    private Role role;

    @BeforeEach
    void setUp() {
        delivery = new Delivery();
        delivery.setUserId(1L);
        delivery.setPassWord("password");
        role = new Role();
        role.setRoleName("delivery");
    }

    @Test
    void testSaveDelivery_Success() {
        when(deliveryRepository.findByUserId(delivery.getUserId())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(roleRepository.findByRoleName("delivery")).thenReturn(Optional.of(role));
        when(deliveryRepository.save(any(Delivery.class))).thenReturn(delivery);

        String result = deliveryService.save(delivery);
        assertEquals("delivery saved", result);
    }

    @Test
    void testSaveDelivery_AlreadyExists() {
        when(deliveryRepository.findByUserId(delivery.getUserId())).thenReturn(Optional.of(delivery));

        String result = deliveryService.save(delivery);
        assertEquals("delivery already exists", result);
    }

    @Test
    void testAssignDelivery_Success() {
        Order order = new Order();
        order.setOrderId(1L);
        order.setStatus(OrderStatus.PENDING);

        DeliveryItem deliveryItem = new DeliveryItem();
        deliveryItem.setDeliveryStatus(DeliveryStatus.PENDING);
        deliveryItem.setOrder(order);
        deliveryItem.setDeliveryPerson(delivery);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(deliveryRepository.findById(1L)).thenReturn(Optional.of(delivery));
        when(deliveryItemRepository.save(any(DeliveryItem.class))).thenReturn(deliveryItem);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(deliveryRepository.save(any(Delivery.class))).thenReturn(delivery);

        String result = deliveryService.assignDelivery(1L, 1L);
        assertEquals("Assignment successful", result);
    }

    @Test
    void testUpdateUser_NotFound() {
        when(deliveryRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> deliveryService.updateUser(1L, new HashMap<>(), null));
    }

    @Test
    void testUpdateUser_Success() {
        Map<String, String> updates = new HashMap<>();
        updates.put("firstName", "John");

        when(deliveryRepository.findById(1L)).thenReturn(Optional.of(delivery));
        when(deliveryRepository.save(any(Delivery.class))).thenReturn(delivery);

        Delivery updatedDelivery = deliveryService.updateUser(1L, updates, null);
        assertEquals("John", updatedDelivery.getFirstName());
    }
}
