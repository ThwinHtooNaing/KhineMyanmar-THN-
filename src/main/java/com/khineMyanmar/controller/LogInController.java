package com.khineMyanmar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.khineMyanmar.model.User;
import com.khineMyanmar.service.UserService;

@Controller
@RequestMapping("/login")
public class LogInController {
	@Autowired
	private UserService userSer;
	@PostMapping("/loginprocess")
	public String loginProcess(Model model,@RequestParam("email")String em,@RequestParam("password")String pass) {
		User user= userSer.checkLogin(em,pass);
		if(user != null) {
		model.addAttribute("user",user);
		return "customer";
		}
		else
			return "redirect:/ecom/index";
	}
	
}
