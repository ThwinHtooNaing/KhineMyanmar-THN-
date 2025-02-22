package com.khineMyanmar.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.khineMyanmar.model.Category;
import com.khineMyanmar.model.Product;
import com.khineMyanmar.model.Shop;
import com.khineMyanmar.repository.IProductRepository;

import jakarta.transaction.Transactional;

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

            String productName = updates.get("productName");
            boolean productExists = productShopService.existsByShopAndProductName(shop, productName);
            if (productExists) {
                return false; // Product already exists in this shop
            }

            // Create new product
            Product product = new Product();
            product.setProductName(updates.get("productName"));
            product.setDescription(updates.get("description"));
            product.setCategory(category);
            product.setProductImagePath("/img/products/product_default.png"); // Default image

            System.out.println("Product "+product);

            product = productRepository.save(product); 

            if (productImage != null && !productImage.isEmpty()) {
                String imageUrl = storageService.saveProductPicture(productImage, shop, product);
                product.setProductImagePath(imageUrl);
                product = productRepository.save(product); // Update product image path in DB
            }

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

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        productRepository.delete(product);
    }

    @Transactional
    public Optional<Product> editProduct(Long id, Shop shop) {
        Optional<Product> productOpt = productRepository.findById(id);
        System.out.println(productOpt.get());
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            return Optional.of(product);
        }
        return Optional.empty();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product updateProduct(Product product, Map<String, String> updates, MultipartFile productImage,Shop shop) throws Exception {
        // Update product details from request parameters
        product.setProductName(updates.get("productName"));
        product.setDescription(updates.get("description"));

        // Update category
        Long categoryId = Long.parseLong(updates.get("categoryId"));
        Category category = categoryService.getCategoryById(categoryId);
        product.setCategory(category);

        if (productImage != null && !productImage.isEmpty()) {
            String imagePath = storageService.saveProductPicture(productImage,shop,product); // Save file
            product.setProductImagePath(imagePath); 
            System.out.println(imagePath);// Set new image path
        }

        double productPrice;
        int stockQuantity;
        try {
            productPrice = Double.parseDouble(updates.get("shopPrice"));
            stockQuantity = Integer.parseInt(updates.get("stockQuantity"));
        } catch (NumberFormatException e) {
            return null; 
        }
        productShopService.updateProduct(product, productPrice, stockQuantity);
        System.out.println(product.getProductImagePath());

        return productRepository.save(product); 
    }

    @Transactional 
    public void deleteProductsByIds(List<Long> productIds) {
        // productRepository.deleteByIdIn(productIds);
        productRepository.deleteByProductIdIn(productIds);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public double getProductPrice(Product product) {
        return productShopService.getProductPriceByProduct(product);
    }

    public double getProductPrice(Long productId) {
        return productShopService.getProductPriceByProductId(productId);
    }


    public List<Product> getTop8Products() {
        return productRepository.findTop8ByOrderByProductNameAsc();
    }




}
