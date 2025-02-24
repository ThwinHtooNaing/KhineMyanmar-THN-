package com.khineMyanmar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.khineMyanmar.model.Delivery;
import com.khineMyanmar.model.ShopOwner;
import com.khineMyanmar.model.User;
import com.khineMyanmar.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class LogInController {

	@Autowired
	private UserService userSer;
	@PostMapping("/loginprocess")
	public String loginProcess(Model model, @RequestParam("email") String email, @RequestParam("password") String password, HttpSession session) {

            User user = userSer.checkLogin(email, password);
            
            if (user != null && user.getRole().getRoleName().equalsIgnoreCase("USER")) 
            {   
                session.setAttribute("customerSession", user); 
                model.addAttribute("customer", user);     
                return "customer/customerIndex"; 
            } else if (user != null && user.getRole().getRoleName().equalsIgnoreCase("ADMIN")) 
            {
                session.setAttribute("adminSession", user);      
                model.addAttribute("admin", user);
                return "admin/adminIndex"; 
            }else if(user != null && user.getRole().getRoleName().equalsIgnoreCase("SHOPOWNER"))
            {
                if (user instanceof ShopOwner) {
                    ShopOwner shopowner = (ShopOwner) user; 
                    session.setAttribute("shopSession", shopowner);
                    model.addAttribute("shopowner", shopowner); 
                }                
                return "shopowner/shopOwnerIndex";
            } else if(user != null && user.getRole().getRoleName().equalsIgnoreCase("DELIVERY"))
            {
                if (user instanceof Delivery) {
                    Delivery delivery = (Delivery) user; 
                    session.setAttribute("deliverySession", delivery);
                    model.addAttribute("delivery", delivery); 
                }                
                return "delivery/deliveryIndex";
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
