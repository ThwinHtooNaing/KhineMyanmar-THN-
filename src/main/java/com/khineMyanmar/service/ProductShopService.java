package com.khineMyanmar.service;

import com.khineMyanmar.model.Product;
import com.khineMyanmar.model.Shop;
import com.khineMyanmar.model.ProductShop;

import org.springframework.beans.factory.annotation.Autowired;
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
}
