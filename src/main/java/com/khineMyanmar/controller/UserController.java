package com.khineMyanmar.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @GetMapping("/product/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
        Product product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
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

    @PostMapping("/cart")
    public ResponseEntity<?> addToCart(@RequestBody Map<String, Object> cartItem, HttpSession session) {
        // Retrieve the cart from the session or create a new one if it doesn't exist
        List<Map<String, Object>> cart = (List<Map<String, Object>>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        // Check if the product already exists in the cart
        boolean exists = false;
        for (Map<String, Object> item : cart) {
            if (item.get("productId").equals(cartItem.get("productId"))) {
                // Update quantity if product already exists
                int currentQty = (int) item.get("quantity");
                int additionalQty = (int) cartItem.get("quantity");
                item.put("quantity", currentQty + additionalQty);
                exists = true;
                break;
            }
        }

        // If not present, add the new cart item
        if (!exists) {
            cart.add(cartItem);
        }

        // Update the session attribute
        session.setAttribute("cart", cart);
        return ResponseEntity.ok(cart);
    }

    @GetMapping("/cart")
    public ResponseEntity<?> getCart(HttpSession session) {
        List<Map<String, Object>> cart = (List<Map<String, Object>>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }
        return ResponseEntity.ok(cart);
    }

    @GetMapping("/cartCount")
    public ResponseEntity<?> getCartCount(HttpSession session) {
        List<Map<String, Object>> cart = (List<Map<String, Object>>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }
        return ResponseEntity.ok(Map.of("cartCount", cart.size()));
    }

	
	
	
}
