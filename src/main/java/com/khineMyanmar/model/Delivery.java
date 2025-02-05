package com.khineMyanmar.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;

@Entity
public class Delivery extends User{
	
	@ManyToOne
	@JoinColumn(name = "shop_id")
	private Shop shop;
	
	@Enumerated(EnumType.STRING)
	private WorkingStatus workingStatus;

	private int deliveryCount;

	@OneToMany(mappedBy = "deliveryPerson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeliveryItem> deliveryItems;
	
	public Delivery(String profilePic, String firstName, String lastName, String phNo, String email, String passWord,
			Role role,Shop shop) {
		super(profilePic, firstName, lastName, phNo, email, passWord, role);
		// TODO Auto-generated constructor stub
		this.shop = shop;
	}
	
	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public Delivery(String profilePic, String firstName, String lastName, String phNo, String email, String passWord,
			Role role) {
		super(profilePic, firstName, lastName, phNo, email, passWord, role);
	}

	public Delivery() {
		// TODO Auto-generated constructor stub
		super();
	}

	public List<DeliveryItem> getDeliveryItems() {
		return deliveryItems;
	}

	public void setDeliveryItems(List<DeliveryItem> deliveryItems) {
		this.deliveryItems = deliveryItems;
	}

	@PrePersist
	public void prePersist() {
		if (workingStatus == null) {
			workingStatus = WorkingStatus.AVAILABLE;
		}
		if (deliveryCount == 0) { // Optional check to prevent overriding manually set values
			deliveryCount = 0;
		}
	}

	
}
