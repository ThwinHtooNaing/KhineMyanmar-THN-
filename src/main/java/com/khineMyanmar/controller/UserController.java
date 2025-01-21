package com.khineMyanmar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.khineMyanmar.model.User;
import com.khineMyanmar.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userSer;
	
	@GetMapping("/usersignup")
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
		return "redirect:/user/usersignup";
	}
	
	
	
	
}
