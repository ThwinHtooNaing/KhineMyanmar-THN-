package com.khineMyanmar.service;

import com.khineMyanmar.model.Product;
import com.khineMyanmar.model.Shop;
import com.khineMyanmar.model.ProductShop;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.khineMyanmar.repository.IProductShopRepository;

@Service
public class ProductShopService {
    
    @Autowired
    private IProductShopRepository productShopRepository;

    public void saveProductAndShop(Product product,double price,int quantity,Shop shop){
        ProductShop productShop = new ProductShop();
        productShop.setProduct(product);
        productShop.setStockQuantity(quantity);
        productShop.setShopPrice(price);
        productShop.setShop(shop);
        productShopRepository.save(productShop);
        System.out.println("ProductShop :"+ productShop);
    }

    public void updateProduct(Product product,double price,int quantity){
        ProductShop productShop = productShopRepository.findByProduct(product).get();
        productShop.setStockQuantity(quantity);
        productShop.setShopPrice(price);
        productShopRepository.save(productShop);
        System.out.println("ProductShop updated :"+ productShop);
    }

    public boolean existsByShopAndProductName(Shop shop, String productName) {
        return productShopRepository.existsByShopAndProduct_ProductName(shop, productName);
    }

    public List<ProductShop> findByShop(Shop shop){
        return productShopRepository.findByShop(shop);
    }

    public Optional<ProductShop> findByProductAndShop(Product product,Shop shop){
        return productShopRepository.findByProductAndShop(product, shop);
    }

    public Page<ProductShop> findByShop(Shop shop, Pageable pageable) {
        return productShopRepository.findByShop(shop, pageable);
    }

}
