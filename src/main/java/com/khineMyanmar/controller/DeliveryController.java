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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.khineMyanmar.DTO.DeliveryItemDTO;
import com.khineMyanmar.model.Delivery;
import com.khineMyanmar.model.DeliveryStatus;
import com.khineMyanmar.model.Shop;
import com.khineMyanmar.model.User;
import com.khineMyanmar.service.DeliveryService;
import com.khineMyanmar.service.ShopService;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/delivery")
public class DeliveryController {
	@Autowired
	private DeliveryService deliveryService;

	@Autowired
	private ShopService shopservice;

	@GetMapping("/deliverysignup")
	public String SignUp(Model model) {
		model.addAttribute("delivery", new Delivery());
		List<Shop > shops = shopservice.allShop();
		model.addAttribute("shops", shops);
		return "deliverySignUp";
	}

	@PostMapping("/deliverysignupprocess")
	public String process(@ModelAttribute Delivery delivery,Model model,@RequestParam Long shopId){
		if(delivery == null) {
			return "redirect:/delivery/deliverysignup";
		}
		
		String message = deliveryService.save(delivery, shopId);
		if(message.equals("delivery saved")) {
			model.addAttribute("delivery", delivery);
			return "delivery/deliveryIndex";	
		}
		return "redirect:/delivery/deliverysignup";
	}

	@RequestMapping("/dashboard")
	public String dashboard(HttpSession session,Model model) {
		Delivery Delivery = (Delivery) session.getAttribute("deliverySession");
        model.addAttribute("delivery", Delivery);    
		return "delivery/deliveryIndex";
	}

	@RequestMapping("/setting")
	public String setting(HttpSession session,Model model) {
		Delivery Delivery = (Delivery) session.getAttribute("deliverySession");
        model.addAttribute("delivery", Delivery);   
		return "delivery/deliverySetting";
	}

	@PostMapping("/updateProfile")
    @ResponseBody
    public ResponseEntity<?> updateProfile(@RequestParam Map<String, String> updates,
                                        @RequestParam(required = false) MultipartFile profileImage,
                                        HttpSession session) {

        Delivery delivery = (Delivery) session.getAttribute("deliverySession");
        Delivery user = deliveryService.getUserByUserId(delivery.getUserId());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Unauthorized"));
        }

        try {
            User updatedUser = deliveryService.updateUser(user.getUserId(), updates, profileImage);
            session.setAttribute("deliverySession", updatedUser);
            return ResponseEntity.ok(Map.of("success", true, "message", "Profile updated successfully!", "updatedUser", updatedUser));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

	@GetMapping("/getDeliveriesByUser")
	@ResponseBody
    public List<DeliveryItemDTO> getAssignedDeliveryItems(HttpSession session) {
        
		Delivery Delivery = (Delivery) session.getAttribute("deliverySession");
		Long deliveryPersonId = Delivery.getUserId(); // Assuming user ID is stored as the username
        List<DeliveryItemDTO> items = deliveryService.getDeliveryItemDTOsForPerson(deliveryPersonId);
        return items;
    }

	@PutMapping("/updateStatus/{id}")
    public ResponseEntity<?> updateDeliveryStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        return deliveryService.updateDeliveryStatus(id, request);
    }

	@GetMapping("/stats")
	@ResponseBody
    public Map<DeliveryStatus, Long> getDeliveryStats(HttpSession session) { 
		Delivery Delivery = (Delivery) session.getAttribute("deliverySession");
		Long deliveryPersonId = Delivery.getUserId(); 
        return deliveryService.getDeliveryStatistics(deliveryPersonId);
    }

	
	
}
