package com.khineMyanmar.DTO;

public class ProductDTO {
    public Long id;
    public String name;
    public String image;
    public String category;
    public double price;
    public int stock;
    public String shopName;

    public ProductDTO(Long id, String name, String image, String category, double price, int stock, String shopName) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.shopName = shopName;
    }
}