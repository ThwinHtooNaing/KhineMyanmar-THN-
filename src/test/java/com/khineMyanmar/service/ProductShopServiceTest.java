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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.khineMyanmar.model.*;
import com.khineMyanmar.repository.IProductShopRepository;

@ExtendWith(MockitoExtension.class)
class ProductShopServiceTest {

    @Mock
    private IProductShopRepository productShopRepository;

    @InjectMocks
    private ProductShopService productShopService;

    private Product product;
    private Shop shop;
    private ProductShop productShop;

    @BeforeEach
    void setUp() {
        shop = new Shop();
        shop.setShopId(1L);

        product = new Product();
        product.setProductId(1L);
        product.setProductName("Laptop");

        productShop = new ProductShop();
        productShop.setProduct(product);
        productShop.setShop(shop);
        productShop.setStockQuantity(10);
        productShop.setShopPrice(1500.0);
    }

    @Test
    void testSaveProductAndShop() {
        when(productShopRepository.save(any(ProductShop.class))).thenReturn(productShop);

        productShopService.saveProductAndShop(product, 1500.0, 10, shop);

        verify(productShopRepository, times(1)).save(any(ProductShop.class));
    }

    @Test
    void testUpdateProduct() {
        when(productShopRepository.findByProduct(product)).thenReturn(Optional.of(productShop));
        when(productShopRepository.save(any(ProductShop.class))).thenReturn(productShop);

        productShopService.updateProduct(product, 1800.0, 5);

        verify(productShopRepository, times(1)).save(any(ProductShop.class));
        assertEquals(1800.0, productShop.getShopPrice());
        assertEquals(5, productShop.getStockQuantity());
    }

    @Test
    void testExistsByShopAndProductName() {
        when(productShopRepository.existsByShopAndProduct_ProductName(shop, "Laptop")).thenReturn(true);

        boolean exists = productShopService.existsByShopAndProductName(shop, "Laptop");
        assertTrue(exists);
    }

    @Test
    void testGetStockQuantityByProduct() {
        when(productShopRepository.findStockQuantityByProduct(product)).thenReturn(Optional.of(productShop));

        int quantity = productShopService.getStockQuantityByProduct(product);
        assertEquals(10, quantity);
    }

    @Test
    void testReduceStockQuantity_Success() {
        when(productShopRepository.reduceStockQuantity(product, 5)).thenReturn(1);

        assertDoesNotThrow(() -> productShopService.reduceStockQuantity(product, 5));
    }

    @Test
    void testReduceStockQuantity_Failure() {
        when(productShopRepository.reduceStockQuantity(product, 15)).thenReturn(0);
        when(productShopRepository.findByProduct(product)).thenReturn(Optional.of(productShop));

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            productShopService.reduceStockQuantity(product, 15));

        assertEquals("Insufficient stock quantity", exception.getMessage());
    }

    @Test
    void testFindByShop() {
        List<ProductShop> productShops = List.of(productShop);
        when(productShopRepository.findByShop(shop)).thenReturn(productShops);

        List<ProductShop> result = productShopService.findByShop(shop);
        assertEquals(1, result.size());
    }

    @Test
    void testFindByShopWithPagination() {
        Page<ProductShop> page = new PageImpl<>(List.of(productShop));
        when(productShopRepository.findByShop(eq(shop), any(PageRequest.class))).thenReturn(page);

        Page<ProductShop> result = productShopService.findByShop(shop, PageRequest.of(0, 5));
        assertEquals(1, result.getContent().size());
    }
}
