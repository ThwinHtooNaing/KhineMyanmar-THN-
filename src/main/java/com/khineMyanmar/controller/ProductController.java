package com.khineMyanmar.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.khineMyanmar.model.Product;
import com.khineMyanmar.model.ProductShop;
import com.khineMyanmar.DTO.ProductDTO;
import com.khineMyanmar.DTO.TopSaleProductDTO;
import com.khineMyanmar.repository.IOrderProductRepository;
import com.khineMyanmar.repository.IProductRepository;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private IOrderProductRepository orderProductRepository;

    @Autowired
    private IProductRepository productRepository;

    @GetMapping("/getAllpaginatedproducts")
    @ResponseBody
    public Page<ProductDTO> getPaginatedProducts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "6") int size,
        @RequestParam(defaultValue = "productName") String sortBy,
        @RequestParam(defaultValue = "ascend") String sortOrder,
        @RequestParam(required = false) String groupBy
    ) {
        Sort.Direction direction = sortOrder.equalsIgnoreCase("descend") ? Sort.Direction.DESC : Sort.Direction.ASC;

        if ("shopname".equalsIgnoreCase(groupBy)) {
            sortBy = "productShops.shop.shopName";
        } else if ("category".equalsIgnoreCase(groupBy)) {
            sortBy = "category.categoryName";
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<Product> productPage = productRepository.findAll(pageable);

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
    @GetMapping("/details/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> getProductDetails(@PathVariable Long id) {
        Product product = productRepository.findById(id).orElse(null);

        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, String> response = new HashMap<>();
        response.put("imagePath", product.getProductImagePath());
        response.put("description", product.getDescription());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/top-sales")
    @ResponseBody
    public List<TopSaleProductDTO> getTopSellingProducts() {
        return orderProductRepository.findTopFourSellingProducts();
    }
}


