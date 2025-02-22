package com.khineMyanmar.DTO;

public class TopSaleProductDTO {
    private String productName;
    private String productImagePath;
    private Double productPrice; // Ensure this matches
    private Long quantitySold;

    public TopSaleProductDTO(String productName, String productImagePath, Double productPrice, Long quantitySold) {
        this.productName = productName;
        this.productImagePath = productImagePath;
        this.productPrice = productPrice;
        this.quantitySold = quantitySold;
    }

    // Getters and setters
    public String getProductName() {
        return productName;
    }

    public String getProductImagePath() {
        return productImagePath;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public Long getQuantitySold() {
        return quantitySold;
    }
}