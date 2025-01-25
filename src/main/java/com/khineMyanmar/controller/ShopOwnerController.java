package com.khineMyanmar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.khineMyanmar.model.ShopOwner;
import com.khineMyanmar.service.ShopOwnerService;

@Controller
@RequestMapping("/shopowner")
public class ShopOwnerController {

	@Autowired
	ShopOwnerService shopOwnerSer;

	@GetMapping("/shopownersignup")
	public String SignUp(Model model) {
		model.addAttribute("shopowner", new ShopOwner());
		return "shopOwnerSignUp";
	}

	@PostMapping("/shopownersignupprocess")
	public String process(@ModelAttribute("user")ShopOwner shopOwner,Model model){
		if(shopOwner == null) {
			return "redirect:/shopowner/shopownersignup";
		}
		
		ShopOwner userex = shopOwnerSer.saveUser(shopOwner);
		if(userex!=null) {
			model.addAttribute("shopOwner", shopOwner);
			return "shopowner/shopOwnerIndex";	
		}
		return "redirect:/shopowner/shopownersignup";
	}

	@RequestMapping("/dashboard")
    public String dashboard(@ModelAttribute("shopowner") ShopOwner user, Model model) {
        model.addAttribute("shopowner", user);
        System.out.println(user.getUserId()+" dashboard");
        return "shopowner/shopOwnerIndex";
    }
    
    @RequestMapping("/deliveries")
    public String user(@ModelAttribute("shopowner") ShopOwner user, Model model) {
        model.addAttribute("shopowner", user);
        System.out.println(user.getUserId()+" deliveries");
        return "shopowner/shopOwnerDeliveries";
    }
        
    @RequestMapping("/products")
    public String product(@ModelAttribute("shopowner") ShopOwner user, Model model) {
        model.addAttribute("shopowner", user);
        System.out.println(user.getUserId()+" products");
        return "shopowner/shopOwnerProducts";
    }
    
    @RequestMapping("/orders")
    public String order(@ModelAttribute("shopowner") ShopOwner user, Model model) {
        model.addAttribute("shopowner", user);
        System.out.println(user.getUserId()+" orders");
        return "shopowner/shopOwnerOrders";
    }
    
    @RequestMapping("/setting")
    public String setting(@ModelAttribute("shopowner") ShopOwner user, Model model) {
        model.addAttribute("shopowner", user);
        System.out.println(user.getUserId()+" setting");
        return "shopowner/shopOwnerSetting";
    } 

	
}
