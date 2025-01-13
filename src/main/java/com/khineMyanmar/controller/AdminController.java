package com.khineMyanmar.controller;
import com.khineMyanmar.model.User;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @RequestMapping("/dashboard")
    public String dashboard(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("user", user);
        return "admin/adminIndex";
    }
    
    @RequestMapping("/users")
    public String user(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("user", user);
        return "admin/adminUsers";
    }
    
    @RequestMapping("/shops")
    public String shop(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("user", user);
        return "admin/adminShops";
    }
    
    @RequestMapping("/products")
    public String product(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("user", user);
        return "admin/adminProducts";
    }
    
    @RequestMapping("/orders")
    public String order(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("user", user);
        return "admin/adminOrders";
    }
    
    @RequestMapping("/setting")
    public String setting(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("user", user);
        return "admin/adminSetting";
    } 
    
}
