package com.khineMyanmar.controller;

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
		model.addAttribute("shops", shops);
		return "deliverySignUp";
	}

	
	
}
