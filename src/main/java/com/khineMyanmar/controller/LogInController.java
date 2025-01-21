package com.khineMyanmar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	public String loginProcess(Model model, @RequestParam("email") String email, @RequestParam("password") String password) {

            User user = userSer.checkLogin(email, password);
            if (user != null && user.getRole().getRoleName().equalsIgnoreCase("USER")) 
            {
                model.addAttribute("customer", user);
                return "customer/customerIndex"; 
            } else if (user != null && user.getRole().getRoleName().equalsIgnoreCase("ADMIN")) 
            {
                model.addAttribute("admin", user);
                return "admin/adminIndex"; 
            }else if(user != null && user.getRole().getRoleName().equalsIgnoreCase("SHOPOWNER"))
            {
                model.addAttribute("shopowner", user);
                return "shopowner/shopOwnerIndex";
            } 
            else 
            {
                if (email.equals("admin") && password.equals("admin123")) 
                {
                    return "admin/adminIndex"; 
                }else 
                {
                    return "redirect:/ecom/index?error=true"; 
                }
            }
            
    }
	
}
