package com.khineMyanmar.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.khineMyanmar.model.Category;
import com.khineMyanmar.model.Delivery;
import com.khineMyanmar.model.Product;
import com.khineMyanmar.model.ProductShop;
import com.khineMyanmar.model.Shop;
import com.khineMyanmar.model.ShopOwner;
import com.khineMyanmar.model.User;
import com.khineMyanmar.service.CategoryService;
import com.khineMyanmar.service.DeliveryService;
import com.khineMyanmar.service.ProductService;
import com.khineMyanmar.service.ProductShopService;
import com.khineMyanmar.service.ShopOwnerService;
import com.khineMyanmar.service.ShopService;
import com.khineMyanmar.service.OrderService;

import jakarta.servlet.http.HttpSession;
@Controller
@RequestMapping("/shopowner")
public class ShopOwnerController {

	@Autowired
	ShopOwnerService shopOwnerSer;

    @Autowired
    ShopService shopService;

    @Autowired
    DeliveryService deliveryService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;

    @Autowired
    ProductShopService productShopService;

    @Autowired
    OrderService orderService;

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
    public String dashboard(Model model , HttpSession session) {
        ShopOwner usersShopOwner = (ShopOwner) session.getAttribute("shopSession");
        model.addAttribute("shopowner", usersShopOwner);       
        return "shopowner/shopOwnerIndex";
    }
    
    @RequestMapping("/deliveries")
    public String user(HttpSession session, Model model) {
        ShopOwner user = (ShopOwner) session.getAttribute("shopSession");
        Delivery newDelivery = new Delivery();
        List<Delivery> deliveries = deliveryService.getAllDeliveriesByShopId(user.getShop());
        model.addAttribute("shopowner", user);
        model.addAttribute("newDelivery", newDelivery);
        model.addAttribute("deliveries", deliveries);
        return "shopowner/shopOwnerDeliveries";
    }
        
    @RequestMapping("/products")
    public String product(HttpSession session, Model model) {
        ShopOwner user=(ShopOwner) session.getAttribute("shopSession");
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("shopowner", user);
        model.addAttribute("categories", categories);
       // System.out.println(user.getUserId()+" products");
        return "shopowner/shopOwnerProducts";
    }
    
    @RequestMapping("/orders")
    public String order(HttpSession session, Model model) {
        ShopOwner user=(ShopOwner) session.getAttribute("shopSession");
        model.addAttribute("shopowner", user);
       // System.out.println(user.getUserId()+" orders");
        return "shopowner/shopOwnerOrders";
    }
    
    @RequestMapping("/setting")
    public String setting(HttpSession session, Model model) {
        ShopOwner user=(ShopOwner) session.getAttribute("shopSession");
        model.addAttribute("shopowner", user);
        System.out.println(user.getUserId()+" setting");
        return "shopowner/shopOwnerSetting";
    } 

    @PostMapping("/updateProfile")
    @ResponseBody
    public ResponseEntity<?> updateProfile(@RequestParam Map<String, String> updates,
                                        @RequestParam(required = false) MultipartFile profileImage,
                                        HttpSession session) {

        ShopOwner shopowner = (ShopOwner) session.getAttribute("shopSession");
        ShopOwner user = shopOwnerSer.getUserByUserId(shopowner.getUserId());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Unauthorized"));
        }

        try {
            User updatedUser = shopOwnerSer.updateUser(user.getUserId(), updates, profileImage);
            session.setAttribute("shopSession", updatedUser);
            return ResponseEntity.ok(Map.of("success", true, "message", "Profile updated successfully!", "updatedUser", updatedUser));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/updateShopProfile")
    @ResponseBody
    public ResponseEntity<?> updateShopProfile(@RequestParam Map<String, String> updates, @RequestParam(required = false) MultipartFile shopprofileImage, HttpSession session) {
        ShopOwner shopowner = (ShopOwner) session.getAttribute("shopSession");
        if (shopowner == null || shopowner.getShop() == null) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "No shop associated with the current session"));
        }
        
        Shop existingShop = shopowner.getShop();
        System.out.println(existingShop);
        try {
            Shop updatedShop = shopService.updateShop(existingShop, updates, shopprofileImage);
            shopowner.setShop(updatedShop);
            session.setAttribute("shopSession", shopowner);
            return ResponseEntity.ok(Map.of("success", true, "message", "Shop updated successfully", "shopId", updatedShop.getShopId()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }


    @PostMapping("/createShop")
    @ResponseBody
    public ResponseEntity<?> postMethodName(@RequestParam Map<String, String> updates,@RequestParam MultipartFile shopprofileImage ,HttpSession session) {
        
        ShopOwner shopowner = (ShopOwner) session.getAttribute("shopSession");
        Long ownerId = shopowner.getUserId();
        if (!updates.containsKey("shopName") || updates.get("shopName").isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Shop name is required"));
        }
        try{
            Shop newshop = shopService.createShop(updates,shopprofileImage,ownerId);
            shopowner.setShop(newshop);
            session.setAttribute("shopSession", shopowner);
            return ResponseEntity.ok(Map.of("success", true, "message", "Shop created successfully", "shopId", newshop));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }

    }  
    @PostMapping("/addDelivery")
	public String postMethodName(@ModelAttribute Delivery delivery,RedirectAttributes redirectAttributes,HttpSession session) {
		
        try {
            if (delivery == null) {
                redirectAttributes.addFlashAttribute("error", "Delivery object is required");
                return "redirect:/shopowner/deliveries";
            }
            ShopOwner shopowner = (ShopOwner) session.getAttribute("shopSession");
            Shop existingShop = shopowner.getShop();
            delivery.setShop(existingShop);
            String message = deliveryService.save(delivery);
            redirectAttributes.addFlashAttribute("success", message);
            return "redirect:/shopowner/deliveries";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/shopowner/deliveries";
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/shopowner/deliveries";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred");
            return "redirect:/shopowner/deliveries";
        }
	}

    @RequestMapping("/addProduct")
    @ResponseBody
    public ResponseEntity<?> addProduct(@RequestParam Map<String, String> updates,
                                        @RequestParam(required = false) MultipartFile profileImage,
                                        HttpSession session) {

        ShopOwner shopOwner = (ShopOwner) session.getAttribute("shopSession");
        if (shopOwner == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); 
        }

        Shop shop = shopOwner.getShop();
        if (shop == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
        }

        // Assuming ProductService.saveProduct returns a boolean
        boolean isSaved = productService.saveProduct(shop, updates, profileImage);

        if (isSaved) {
            return new ResponseEntity<>(HttpStatus.OK); // Success response with no body
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Error response with no body
        }
    }    

    @GetMapping("/getAllProducts")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getAllProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "6") int size, // Set default size to 6
            HttpSession session) {

        ShopOwner shopOwner = (ShopOwner) session.getAttribute("shopSession");
        if (shopOwner == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Shop shop = shopOwner.getShop();
        if (shop == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Fetch paginated products
        Page<ProductShop> productPage = productShopService.findByShop(shop, PageRequest.of(page - 1, size));
        
        List<Map<String, Object>> products = new ArrayList<>();
        
        for (ProductShop ps : productPage.getContent()) {
            Product product = ps.getProduct();
            Map<String, Object> productData = new HashMap<>();
            productData.put("id", product.getProductId());
            productData.put("name", product.getProductName());
            productData.put("image", product.getProductImagePath());
            productData.put("price", ps.getShopPrice());
            productData.put("quantity", ps.getStockQuantity());
            productData.put("category", product.getCategory().getCategoryName());
            products.add(productData);
        }

        // Return paginated response
        Map<String, Object> response = new HashMap<>();
        response.put("products", products);
        response.put("totalProducts", productPage.getTotalElements());
        response.put("totalPages", productPage.getTotalPages());
        response.put("currentPage", page);

        return ResponseEntity.ok(response);
    }

	
    @DeleteMapping("/deleteProduct/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/editProduct")
    @ResponseBody
    public ResponseEntity<?> editProduct(@RequestBody Map<String, Long> requestData, HttpSession session) {
        ShopOwner shopOwner = (ShopOwner) session.getAttribute("shopSession");
        
        if (shopOwner == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: Unauthorized access.");
        }

        Long id = requestData.get("id");
        if (id == null) {
            return ResponseEntity.badRequest().body("Error: Product ID is required.");
        }

        System.out.println("Shop owner: " + shopOwner.getFirstName() + " " + shopOwner.getLastName() + " requested product ID: " + id);

        Shop shop = shopOwner.getShop();
        if (shop == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Shop not found for this owner.");
        }

        Optional<Product> productOpt = productService.editProduct(id, shop);
        return productOpt
            .map(product -> ResponseEntity.ok().body(product)) // Ensure consistent return type
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    
    @PostMapping("/updateProduct")
    @ResponseBody
    public ResponseEntity<?> updateProduct(
            @RequestParam Map<String, String> updates,
            @RequestParam(required = false) MultipartFile productImage,
            HttpSession session) {
        
        // Get the shop owner session
        
        ShopOwner shopowner = (ShopOwner) session.getAttribute("shopSession");
        if (shopowner == null || shopowner.getShop() == null) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "No shop associated with the current session"));
        }

        Shop shop = shopowner.getShop();

        // Validate if the product exists
        Long productId;
        try {
            productId = Long.parseLong(updates.get("id"));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Invalid product ID"));
        }
        System.out.println(productImage);
        Product existingProduct = productService.getProductById(productId);
        if (existingProduct == null) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Product not found"));
        }

        try {
            // Call service to update product details
            Product updatedProduct = productService.updateProduct(existingProduct, updates, productImage,shop);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Product updated successfully",
                    "productId", updatedProduct.getProductId()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/deleteProducts")
    @ResponseBody
    public ResponseEntity<?> deleteProducts(@RequestBody Map<String, List<Long>> request) {
        List<Long> productIds = request.get("productIds");
        System.out.println(productIds);
        if (productIds == null || productIds.isEmpty()) {
            return ResponseEntity.badRequest().body("No products selected for deletion.");
        }

        System.out.println(productIds);

        // Call service layer to delete products by their IDs
        productService.deleteProductsByIds(productIds);

        return ResponseEntity.ok().body(Map.of("success", true));
    }

    @GetMapping("/stats")
    @ResponseBody
    public Map<String, Object> getDashboardStats() {
        return orderService.getDashboardStats();
    }



    
}