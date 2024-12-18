package com.khineMyanmar.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productShopId;

    @ManyToOne
    @JoinColumn(name = "order_id", insertable = false, updatable = false, referencedColumnName = "orderId")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", insertable = false, updatable = false, referencedColumnName = "productId")
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double soldPrice;

    @Column(nullable = false)
    private double subTotal;

    // Getters and setters
    public Long getId() {
        return productShopId;
    }

    public void setId(Long id) {
        this.productShopId = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSoldPrice() {
        return soldPrice;
    }

    public void setSoldPrice(double soldPrice) {
        this.soldPrice = soldPrice;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    // Constructors
    public OrderProduct() {}

    public OrderProduct(Long id, Order order, Product product, int quantity, double soldPrice, double subTotal) {
        this.productShopId = id;
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.soldPrice = soldPrice;
        this.subTotal = subTotal;
    }
}
