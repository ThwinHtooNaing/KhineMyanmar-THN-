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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.khineMyanmar.model.Delivery;
import com.khineMyanmar.model.Shop;
import com.khineMyanmar.model.ShopOwner;
import com.khineMyanmar.model.User;
import com.khineMyanmar.service.DeliveryService;
import com.khineMyanmar.service.ShopOwnerService;
import com.khineMyanmar.service.ShopService;

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
        model.addAttribute("shopowner", user);
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

	
}