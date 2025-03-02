package com.khineMyanmar.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.khineMyanmar.model.Shop;
import com.khineMyanmar.model.ShopOwner;
import com.khineMyanmar.repository.IShopOwnerRepository;
import com.khineMyanmar.repository.IShopRepository;

@ExtendWith(MockitoExtension.class)
public class ShopServiceTest {
    
    @Mock
    private IShopRepository shopRep;

    @Mock
    private IShopOwnerRepository shopOwnerRepository;

    @Mock
    private StorageService storageService;

    @InjectMocks
    private ShopService shopService;

    private Shop shop;
    private ShopOwner owner;

    @BeforeEach
    void setUp() {
        shop = new Shop();
        shop.setShopId(1L);
        shop.setShopName("Test Shop");
        shop.setAddress("Test Address");
        shop.setContactNumber("123456789");
        shop.setShopImagePath("/img/shopprofiles/default.png");
        
        owner = new ShopOwner();
        owner.setUserId(1L);
    }

    @Test
    void testGetAllShops() {
        List<Shop> shops = Arrays.asList(shop);
        when(shopRep.findAll()).thenReturn(shops);

        List<Shop> result = shopService.getAllShops();
        assertEquals(1, result.size());
        assertEquals("Test Shop", result.get(0).getShopName());
    }

    @Test
    void testGetShopById_Success() {
        when(shopRep.findById(1L)).thenReturn(Optional.of(shop));

        Shop result = shopService.getShopById(1L);
        assertNotNull(result);
        assertEquals("Test Shop", result.getShopName());
    }

    @Test
    void testGetShopById_NotFound() {
        when(shopRep.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> shopService.getShopById(1L));
        assertEquals("Shop not found", exception.getMessage());
    }

    @Test
    void testCreateShop_Success() {
        when(shopOwnerRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(shopRep.save(any(Shop.class))).thenReturn(shop);

        Map<String, String> updates = new HashMap<>();
        updates.put("shopName", "New Shop");
        updates.put("address", "New Address");
        updates.put("contactNumber", "987654321");
        
        Shop result = shopService.createShop(updates, null, 1L);

        assertNotNull(result);
        assertEquals("New Shop", result.getShopName());
    }

    @Test
    void testUpdateShop_Success() {
        when(shopRep.save(any(Shop.class))).thenReturn(shop);

        Map<String, String> updates = new HashMap<>();
        updates.put("shopName", "Updated Shop");
        updates.put("address", "Updated Address");
        updates.put("contactNumber", "123123123");
        
        Shop result = shopService.updateShop(shop, updates, null);
        
        assertNotNull(result);
        assertEquals("Updated Shop", result.getShopName());
    }

    @Test
    void testUpdateShop_NotFound() {
        Exception exception = assertThrows(RuntimeException.class, () -> shopService.updateShop(null, new HashMap<>(), null));
        assertEquals("Shop not found", exception.getMessage());
    }
}
