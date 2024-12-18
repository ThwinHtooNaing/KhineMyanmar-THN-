package com.khineMyanmar.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.khineMyanmar.model.Shop;
import com.khineMyanmar.repository.IShopRepository;
@Service
public class ShopService {
	@Autowired
	private IShopRepository shopRep;

	public List<Shop> allShop() {
		// TODO Auto-generated method stub
		return shopRep.findAll();
	}

}
