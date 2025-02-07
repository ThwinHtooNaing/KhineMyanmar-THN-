package com.khineMyanmar.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ProductShop {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long productShopId;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    
    @Column(nullable = false)
    private int stockQuantity;
    
    @Column(nullable = false)
    private double productPrice;

	public Long getProductShopId() {
		return productShopId;
	}

	public void setProductShopId(Long productShopId) {
		this.productShopId = productShopId;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(int stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	public double getShopPrice() {
		return productPrice;
	}

	public void setShopPrice(double shopPrice) {
		this.productPrice = shopPrice;
	}
	
	public ProductShop(Shop shop, Product product, int stockQuantity, double shopPrice) {
		super();
		this.shop = shop;
		this.product = product;
		this.stockQuantity = stockQuantity;
		this.productPrice = shopPrice;
	}

	public ProductShop() {
		super();
	}	
    
}
