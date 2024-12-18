package com.khineMyanmar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.khineMyanmar.model.ShopOwner;

@Controller
@RequestMapping("/shopowner")
public class ShopOwnerController {

	@GetMapping("/shopownersignup")
	public String SignUp(Model model) {
		model.addAttribute("shopowner", new ShopOwner());
		return "shopAdminSignUp";
	}
	
}
