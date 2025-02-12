package com.khineMyanmar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.khineMyanmar.model.User;
import com.khineMyanmar.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/customer")
public class UserController {
	@Autowired
	private UserService userSer;
	
	@GetMapping("/customersignup")
	public String signUp(Model model) {
		model.addAttribute("user", new User());
		return "userSignUp";
	}
	
	@GetMapping("/signupprocess")
	public String process(@ModelAttribute("user")User user,Model model) {
		if(user == null) {
			return "redirect:/user/usersignup";
		}
		
		User userex = userSer.saveUser(user);
		if(userex!=null) {
			model.addAttribute("user", user);
			return "customer/customerIndex";	
		}
		return "redirect:/customer/customersignup";
	}

	@RequestMapping("/index")
    public String index(HttpSession session, Model model) {
        User user = (User) session.getAttribute("customerSession");
        model.addAttribute("customer", user);
        return "customer/customerIndex";
    }
	
	@RequestMapping("/shop")
    public String shopsPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("customerSession");
        model.addAttribute("customer", user);
        return "customer/customerShop";
    }

	@RequestMapping("/product")
    public String productsPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("customerSession");
        model.addAttribute("customer", user);
        return "customer/customerProduct";
    }

	@RequestMapping("/profile")
    public String profilePage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("customerSession");
        model.addAttribute("customer", user);
        return "customer/customerProfile";
    }

	@RequestMapping("/aboutUs")
    public String aboutUsPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("customerSession");
        model.addAttribute("customer", user);
        return "customer/customerAboutUs";
    }

	@RequestMapping("/cart")
    public String cartPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("customerSession");
        model.addAttribute("customer", user);
        return "customer/customerCart";
    }

	@RequestMapping("/history")
    public String historyPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("customerSession");
        model.addAttribute("customer", user);
        return "customer/customerHistory";
    }

	
	
	
}
