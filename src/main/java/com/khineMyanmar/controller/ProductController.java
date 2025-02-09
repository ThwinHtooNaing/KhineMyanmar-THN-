package com.khineMyanmar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.khineMyanmar.model.Product;
import com.khineMyanmar.model.ProductShop;
import com.khineMyanmar.DTO.ProductDTO;
import com.khineMyanmar.repository.IProductRepository;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private IProductRepository productRepository;

    @GetMapping("/getAllpaginatedproducts")
    @ResponseBody
    public Page<ProductDTO> getPaginatedProducts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "6") int size,
        @RequestParam(defaultValue = "productName") String sortBy,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String shop
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        Page<Product> productPage;
        if (category != null && !category.isEmpty()) {
            productPage = productRepository.findByCategory_CategoryName(category, pageable);
        } else if (shop != null && !shop.isEmpty()) {
            productPage = productRepository.findByProductShops_Shop_ShopName(shop, pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }

        return productPage.map(product -> new ProductDTO(
            product.getProductId(),
            product.getProductName(),
            product.getProductImagePath(),
            product.getCategory() != null ? product.getCategory().getCategoryName() : "",
            product.getProductShops().stream().findFirst().map(ProductShop::getShopPrice).orElse(0.0),
            product.getProductShops().stream().findFirst().map(ProductShop::getStockQuantity).orElse(0),
            product.getProductShops().stream().findFirst().map(ps -> ps.getShop().getShopName()).orElse("")
        ));
    }
}


