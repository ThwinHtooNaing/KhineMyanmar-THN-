package com.khineMyanmar.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.khineMyanmar.model.Category;
import com.khineMyanmar.model.Product;
import com.khineMyanmar.model.Shop;
import com.khineMyanmar.model.User;
import com.khineMyanmar.service.CategoryService;
import com.khineMyanmar.service.ProductService;
import com.khineMyanmar.service.ShopService;
import com.khineMyanmar.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/customer")
public class UserController {

	@Autowired
	private UserService userSer;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ShopService shopService;
	
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

    @GetMapping("/shops")
    public ResponseEntity<List<Shop>> getShops(){
        List<Shop> shops = shopService.getFirst6Shops();
        return ResponseEntity.ok(shops);
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(){
        List<Product> products = productService.getTop8Products();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
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

    @PostMapping("/updateProfile")
    @ResponseBody
    public ResponseEntity<?> updateProfile(@RequestParam Map<String, String> updates,
                                        @RequestParam(required = false) MultipartFile profileImage,
                                        HttpSession session) {

        User customer = (User) session.getAttribute("customerSession");
        User user = userSer.getUserByUserId(customer.getUserId());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Unauthorized"));
        }

        try {
            User updatedUser = userSer.updateUser(user.getUserId(), updates, profileImage);
            session.setAttribute("customerSession", updatedUser);
            return ResponseEntity.ok(Map.of("success", true, "message", "Profile updated successfully!", "updatedUser", updatedUser));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

	
	
	
}
