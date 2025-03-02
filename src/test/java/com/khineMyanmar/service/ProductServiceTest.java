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
import com.khineMyanmar.repository.IProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private IProductRepository productRepository;

    @Mock
    private ProductShopService productShopService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private StorageService storageService;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private Shop shop;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setCategoryId(1L);
        category.setCategoryName("Electronics");

        shop = new Shop();
        shop.setShopId(1L);

        product = new Product();
        product.setProductId(1L);
        product.setProductName("Laptop");
        product.setCategory(category);
    }

    @Test
    void testSaveProduct_Success() {
        Map<String, String> updates = Map.of(
            "categoryId", "1",
            "productName", "Laptop",
            "description", "Gaming Laptop",
            "productPrice", "1500",
            "stockQuantity", "10"
        );

        when(categoryService.getCategoryById(1L)).thenReturn(category);
        when(productShopService.existsByShopAndProductName(shop, "Laptop")).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        boolean result = productService.saveProduct(shop, updates, null);
        assertTrue(result);
    }

    @Test
    void testSaveProduct_MissingRequiredFields() {
        Map<String, String> updates = Map.of(
            "productName", "Laptop",
            "description", "Gaming Laptop"
        );

        boolean result = productService.saveProduct(shop, updates, null);
        assertFalse(result);
    }

    @Test
    void testSaveProduct_DuplicateProduct() {
        Map<String, String> updates = Map.of(
            "categoryId", "1",
            "productName", "Laptop",
            "description", "Gaming Laptop",
            "productPrice", "1500",
            "stockQuantity", "10"
        );

        when(productShopService.existsByShopAndProductName(shop, "Laptop")).thenReturn(true);

        boolean result = productService.saveProduct(shop, updates, null);
        assertFalse(result);
    }

    @Test
    void testSaveProduct_InvalidCategory() {
        Map<String, String> updates = Map.of(
            "categoryId", "99",
            "productName", "Laptop",
            "description", "Gaming Laptop",
            "productPrice", "1500",
            "stockQuantity", "10"
        );

        when(categoryService.getCategoryById(99L)).thenReturn(null);

        boolean result = productService.saveProduct(shop, updates, null);
        assertFalse(result);
    }

    @Test
    void testSaveProduct_InvalidPriceOrQuantity() {
        Map<String, String> updates = Map.of(
            "categoryId", "1",
            "productName", "Laptop",
            "description", "Gaming Laptop",
            "productPrice", "abc",
            "stockQuantity", "xyz"
        );

        boolean result = productService.saveProduct(shop, updates, null);
        assertFalse(result);
    }

    @Test
    void testDeleteProduct_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void testDeleteProductsByIds_Success() {
        List<Long> productIds = List.of(1L, 2L, 3L);
        productService.deleteProductsByIds(productIds);

        verify(productRepository, times(1)).deleteByProductIdIn(productIds);
    }

    @Test
    void testGetProductById_Found() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product foundProduct = productService.getProductById(1L);
        assertNotNull(foundProduct);
        assertEquals(1L, foundProduct.getProductId());
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        Product foundProduct = productService.getProductById(99L);
        assertNull(foundProduct);
    }

    @Test
    void testUpdateProduct_Success() throws Exception {
        Map<String, String> updates = Map.of(
            "productName", "Gaming Laptop",
            "description", "High-end gaming",
            "categoryId", "1",
            "shopPrice", "1800",
            "stockQuantity", "5"
        );

        when(categoryService.getCategoryById(1L)).thenReturn(category);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product updatedProduct = productService.updateProduct(product, updates, null, shop);

        assertNotNull(updatedProduct);
        assertEquals("Gaming Laptop", updatedProduct.getProductName());
        assertEquals("High-end gaming", updatedProduct.getDescription());
    }

    @Test
    void testUpdateProduct_InvalidCategory() throws Exception {
        Map<String, String> updates = Map.of(
            "categoryId", "99",
            "productName", "Gaming Laptop"
        );

        when(categoryService.getCategoryById(99L)).thenReturn(null);

        Product updatedProduct = productService.updateProduct(product, updates, null, shop);

        assertNull(updatedProduct);
    }

    @Test
    void testGetProductsByCategory() {
        List<Product> products = List.of(product);
        Page<Product> productPage = new PageImpl<>(products);

        when(productRepository.findByCategory_CategoryName(eq("Electronics"), any(PageRequest.class)))
            .thenReturn(productPage);

        List<Product> result = productService.getProductsByCategory(0, 5, "Electronics");
        assertEquals(1, result.size());
    }

    @Test
    void testGetProductsByShopId() {
        List<Product> products = List.of(product);

        when(productRepository.findProductsByShopId(1L)).thenReturn(products);

        List<Product> result = productService.getProductsByShopId(1L, 0, 5);
        assertEquals(1, result.size());
    }
}
