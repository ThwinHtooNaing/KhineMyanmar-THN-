package com.khineMyanmar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
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
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userSer;
	@PostMapping("/loginprocess")
	public String loginProcess(Model model, @RequestParam("email") String email, @RequestParam("password") String password) {
        try {
            // Authenticate user manually
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
            );

            // Set authentication context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Check if the user exists in the database
            User user = userSer.checkLogin(email, password);
            model.addAttribute("user", user);
            if (user != null && user.getRole().getRoleName().equalsIgnoreCase("USER")) {
                return "customer"; // Redirect to customer page
            } else if (user != null && user.getRole().getRoleName().equalsIgnoreCase("ADMIN")) {
                
                return "admin/adminIndex"; // Redirect to admin dashboard for ADMIN role
            } else {
                // User might be in-memory (like "admin")
                if (email.equals("admin") && password.equals("admin123")) {
                    return "admin/adminIndex"; // Redirect to admin dashboard
                }else {
                	System.out.println("Error");
                    return "redirect:/ecom/index?error=true"; 
                }
            }
            

        } catch (AuthenticationException e) {
            // Handle authentication failure
        	System.out.println("Ex");
            return "redirect:/ecom/index";
        }
    }
	
}
