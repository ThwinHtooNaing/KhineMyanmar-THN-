package com.khineMyanmar.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "`order`")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO) 
	private Long orderId;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	
	private User user;
	
	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	private LocalDate checkoutDate;

	@Enumerated(EnumType.STRING) // Store enum values as strings in the database
	@Column(nullable = true)
	private PaymentStatus paymentStatus;
	
	private Double amount;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderProduct> orderProducts = new HashSet<>();
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = true)
    @JoinColumn(name = "delivery_item_id", nullable = true)
    private DeliveryItem deliveryItem;

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}
	
    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Order(User user, OrderStatus status, Double amount) {
		super();
		this.user = user;
		this.status = status;
		this.amount = amount;
	}

	public DeliveryItem getDeliveryItem() {
		return deliveryItem;
	}

	public void setDeliveryItem(DeliveryItem deliveryItem) {
		this.deliveryItem = deliveryItem;
	}


	public Set<OrderProduct> getOrderProducts() {
		return orderProducts;
	}

	public void setOrderProducts(Set<OrderProduct> orderProducts) {
		this.orderProducts = orderProducts;
	}

	public LocalDate getCheckoutDate() {
    return checkoutDate;
	}

	public void setCheckoutDate(LocalDate checkoutDate) {
		this.checkoutDate = checkoutDate;
	}

	public Order() {
		super();
	}

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", user=" + user + ", status=" + status + ", amount=" + amount + "]";
    }
		
}
