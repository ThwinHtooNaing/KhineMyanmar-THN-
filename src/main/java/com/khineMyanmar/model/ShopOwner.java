package com.khineMyanmar.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;

@Entity
public class ShopOwner extends User{

	@OneToOne(mappedBy = "shopOwner", cascade = CascadeType.ALL, optional = true)
	@JsonIgnore 
    private Shop shop;
	
	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public ShopOwner(String profilePic, String firstName, String lastName, String phNo, String email, String passWord,
			Role role) {
		super(profilePic, firstName, lastName, phNo, email, passWord, role);
		
	}
	
	public ShopOwner() {
		super();
	}
	

}
