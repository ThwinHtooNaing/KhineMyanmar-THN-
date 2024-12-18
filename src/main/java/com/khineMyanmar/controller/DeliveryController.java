package com.khineMyanmar.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.khineMyanmar.model.Delivery;
import com.khineMyanmar.model.Shop;
import com.khineMyanmar.service.ShopService;

@Controller
@RequestMapping("/delivery")
public class DeliveryController {
	@Autowired
	private ShopService shopservice;

	@GetMapping("/deliverysignup")
	public String SignUp(Model model) {
		model.addAttribute("delivery", new Delivery());
		List<Shop > shops = shopservice.allShop();
		List<String>shopname = new ArrayList<String>();
//		shopname.add("select shop");
		
		shopname.add("Hello");
		shopname.add("World");
		for(Shop s:shops) {
			
			shopname.addLast(s.getShopName());
		}
		model.addAttribute("shopname", shopname);
		return "deliverySignUp";
	}
}
