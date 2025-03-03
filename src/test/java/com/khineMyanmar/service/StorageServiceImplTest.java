package com.khineMyanmar.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.khineMyanmar.model.Product;
import com.khineMyanmar.model.Shop;

@ExtendWith(MockitoExtension.class)
class StorageServiceImplTest {
    
    @InjectMocks
    private StorageServiceImpl storageService;
    
    private MultipartFile mockFile;
    private Shop shop;
    private Product product;

    @BeforeEach
    void setUp() {
        mockFile = new MockMultipartFile(
            "file", "test-image.jpg", "image/jpeg", "test data".getBytes()
        );
        
        shop = new Shop();
        shop.setShopId(1L);
        shop.setShopName("TestShop");
        
        product = new Product();
        product.setProductId(1L);
        product.setProductName("TestProduct");
    }

    @Test
    void testSaveProfilePicture_Success() throws IOException {
        String result = storageService.saveProfilePicture(mockFile, "John", "Doe", 1L, "admin");
        assertNotNull(result);
        assertTrue(result.contains("/img/profiles/admin/john_doe_1/"));
    }

    @Test
    void testSaveProfilePicture_FileIsEmpty() {
        MultipartFile emptyFile = new MockMultipartFile("file", "", "image/jpeg", new byte[0]);
        Exception exception = assertThrows(RuntimeException.class, () -> 
            storageService.saveProfilePicture(emptyFile, "John", "Doe", 1L, "admin")
        );
        assertEquals("File is empty or null", exception.getMessage());
    }

    @Test
    void testSaveShopProfilePicture_Success() throws IOException {
        String result = storageService.saveShopProfilePicture(mockFile, shop.getShopName(), shop.getShopId());
        assertNotNull(result);
        System.out.println(result);
        assertTrue(result.contains("/img/shopprofiles/testshop_1/"));
    }

    @Test
    void testSaveShopProfilePicture_FileIsEmpty() {
        MultipartFile emptyFile = new MockMultipartFile("file", "", "image/jpeg", new byte[0]);
        Exception exception = assertThrows(RuntimeException.class, () -> 
            storageService.saveShopProfilePicture(emptyFile, shop.getShopName(), shop.getShopId())
        );
        assertEquals("File is empty or null", exception.getMessage());
    }

    @Test
    void testSaveProductPicture_Success() throws IOException {
        String result = storageService.saveProductPicture(mockFile, shop, product);
        assertNotNull(result);
        System.out.println(result);
        assertTrue(result.contains("/img/products/1_TestShop/testproduct_1/"));
    }

    @Test
    void testSaveProductPicture_FileIsEmpty() {
        MultipartFile emptyFile = new MockMultipartFile("file", "", "image/jpeg", new byte[0]);
        Exception exception = assertThrows(RuntimeException.class, () -> 
            storageService.saveProductPicture(emptyFile, shop, product)
        );
        assertEquals("File is empty or null", exception.getMessage());
    }
}
