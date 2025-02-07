package com.khineMyanmar.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.khineMyanmar.model.Category;
import com.khineMyanmar.model.Product;
import com.khineMyanmar.model.Shop;
import com.khineMyanmar.repository.IProductRepository;

@Service
public class ProductService {
    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private ProductShopService productShopService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private StorageService storageService;

    public boolean saveProduct(Shop shop, Map<String, String> updates, MultipartFile productImage) {
        try {
            // Validate required fields
            if (!updates.containsKey("categoryId") || !updates.containsKey("productName") || 
                !updates.containsKey("description") || !updates.containsKey("productPrice") || 
                !updates.containsKey("stockQuantity")) {
                return false; // Missing required fields
            }

            Long categoryId = Long.parseLong(updates.get("categoryId"));
            Category category = categoryService.getCategoryById(categoryId);
            System.out.println("categoryId: " + category);
            if (category == null) {
                return false; // Invalid category
            }

            // Create new product
            Product product = new Product();
            product.setProductName(updates.get("productName"));
            product.setDescription(updates.get("description"));
            product.setCategory(category);
            product.setProductImagePath("/img/products/product_default.png"); // Default image

            System.out.println("Product "+product);

            product = productRepository.save(product); // Save product to get ID

            // Handle product image upload
            if (productImage != null && !productImage.isEmpty()) {
                String imageUrl = storageService.saveProductPicture(productImage, shop, product);
                product.setProductImagePath(imageUrl);
                productRepository.save(product); // Update product image path in DB
            }

            // Convert price and stock quantity safely
            double productPrice;
            int stockQuantity;
            try {
                productPrice = Double.parseDouble(updates.get("productPrice"));
                stockQuantity = Integer.parseInt(updates.get("stockQuantity"));
            } catch (NumberFormatException e) {
                return false; // Invalid price or quantity
            }

            // Save product-shop relationship
            productShopService.saveProductAndShop(product, productPrice, stockQuantity, shop);
            System.out.println("ProductShop");

            return true;
        } catch (Exception e) {
            return false; // Handle unexpected errors
        }
    }

}
