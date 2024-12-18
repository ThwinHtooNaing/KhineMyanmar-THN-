package com.khineMyanmar.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Shop {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long shopId;
	
	@Column(nullable = false)
	private String shopImagePath;
	
	@Column(nullable = false)
	private String shopName;
	
	@Column(nullable = true)
	private String address;
	
	@Column(nullable = false)
	private String contactNumber;
	
	@OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ProductShop> productShops = new HashSet<>();

	public Set<ProductShop> getProductShops() {
	    return productShops;
	}

	public void setProductShops(Set<ProductShop> productShops) {
	    this.productShops = productShops;
	}

	@OneToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "userId") // Foreign key references ShopOwner
    private ShopOwner shopOwner;
	
	@OneToMany(mappedBy = "shop")
	private Set<Delivery> deliveries;
	
	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getShopImagePath() {
		return shopImagePath;
	}

	public void setShopImagePath(String shopImagePath) {
		this.shopImagePath = shopImagePath;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public ShopOwner getShopOwner() {
		return shopOwner;
	}

	public void setShopOwner(ShopOwner shopOwner) {
		this.shopOwner = shopOwner;
	}

	public Set<Delivery> getDeliveries() {
		return deliveries;
	}

	public void setDeliveries(Set<Delivery> deliveries) {
		this.deliveries = deliveries;
	}

	
	public Shop(String shopImagePath, String shopName, String address, String contactNumber) {
		super();
		this.shopImagePath = shopImagePath;
		this.shopName = shopName;
		this.address = address;
		this.contactNumber = contactNumber;
	}

	public Shop() {
		super();
	}
	
}
