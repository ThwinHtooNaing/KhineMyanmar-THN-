package com.khineMyanmar.controller;
import com.khineMyanmar.model.Category;
import com.khineMyanmar.model.Shop;
import com.khineMyanmar.model.User;
import com.khineMyanmar.service.CategoryService;
import com.khineMyanmar.service.RoleService;
import com.khineMyanmar.service.ShopService;
import com.khineMyanmar.service.UserService;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private ShopService shopService;

    @RequestMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User admin = (User) session.getAttribute("adminSession");
        model.addAttribute("admin", admin);
        return "admin/adminIndex";
    }
    
    @RequestMapping("/users")
    public String user(HttpSession session, Model model) {
        User admin = (User) session.getAttribute("adminSession");
        List<User> users = userService.getAllUsers();
        model.addAttribute("admin", admin);
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("newUser",new User());
        model.addAttribute("users", users);
        return "admin/adminUsers";
    }
    
    @RequestMapping("/shops")
    public String shop(HttpSession session, Model model) {
        User admin = (User) session.getAttribute("adminSession");
        List<Shop> shops = shopService.getAllShops();
        model.addAttribute("shops", shops);
        model.addAttribute("admin", admin);
        return "admin/adminShops";
    }
    
    @RequestMapping("/products")
    public String product(HttpSession session, Model model) {
        User admin = (User) session.getAttribute("adminSession");
        model.addAttribute("admin", admin);
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "admin/adminProducts";
    }
    
    @RequestMapping("/orders")
    public String order(HttpSession session, Model model) {
        User admin = (User) session.getAttribute("adminSession");
        model.addAttribute("admin", admin);
        return "admin/adminOrders";
    }
    
    @RequestMapping("/setting")
    public String setting(HttpSession session, Model model) {
        User admin = (User) session.getAttribute("adminSession");
        model.addAttribute("admin", admin);
        return "admin/adminSetting";
    } 

    @PostMapping("/addcategories")
    @ResponseBody
    public ResponseEntity<Map<String, String>> addCategory(@RequestBody Map<String, String> request) {
        System.out.println("Received request: " + request); // Debugging

        try {
            String categoryName = request.get("name");

            if (categoryName == null || categoryName.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Category name is required"));
            }

            String message = categoryService.addCategory(categoryName);
            return ResponseEntity.ok(Collections.singletonMap("message", message));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace(); // Print any unexpected errors in the server logs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "An unexpected error occurred"));
        }
    }

    @PostMapping("/categoryupdate")
    @ResponseBody
    public ResponseEntity<Map<String, String>> updateCategory(@RequestParam("categoryId") Long id, 
                                                              @RequestParam("categoryName") String name) {
        Category category = categoryService.getCategoryById(id);
        if (category != null) {
            category.setCategoryName(name);
            categoryService.saveCategory(category);
            return ResponseEntity.ok(Collections.singletonMap("message", "Updated Successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("error", "Category Not Found"));
    }
    

    @PostMapping("/categorydelete")
    @ResponseBody
    public ResponseEntity<Map<String, String>> deleteCategory(@RequestParam("categoryId") Long id) {
        Category category = categoryService.getCategoryById(id);
        if (category != null) {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok(Collections.singletonMap("message", "Deleted Successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("error", "Category Not Found"));
    }

    @GetMapping("/checkChildren")
    @ResponseBody
    public Map<String, Boolean> checkChildren(@RequestParam Long categoryId) {
        Category category = categoryService.getCategoryById(categoryId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("hasChildren", category != null && !category.getProducts().isEmpty());
        return response;
    }

    @PostMapping("/adduser")
    public String addUser(@ModelAttribute User user, @RequestParam("roleName") String roleName, RedirectAttributes redirectAttributes) {
        try {
            if (user == null) {
                redirectAttributes.addFlashAttribute("error", "User object is required");
                return "redirect:/admin/users";
            }

            String message = userService.save(user, roleName);
            redirectAttributes.addFlashAttribute("success", message);
            return "redirect:/admin/users";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/users";
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/users";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred");
            return "redirect:/admin/users";
        }
    }

    @GetMapping("/getPaginatedUsers")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<User> usersPage = userService.getUsers(PageRequest.of(page, size));
        Map<String, Object> response = new HashMap<>();
        response.put("users", usersPage.getContent());
        response.put("totalPages", usersPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/updateProfile")
    @ResponseBody
    public ResponseEntity<?> updateProfile(@RequestParam Map<String, String> updates,
                                        @RequestParam(required = false) MultipartFile profileImage,
                                        HttpSession session) {

        User admin = (User) session.getAttribute("adminSession");
        User user = userService.getUserByUserId(admin.getUserId());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Unauthorized"));
        }

        try {
            User updatedUser = userService.updateUser(user.getUserId(), updates, profileImage);
            session.setAttribute("adminSession", updatedUser);
            return ResponseEntity.ok(Map.of("success", true, "message", "Profile updated successfully!", "updatedUser", updatedUser));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }




    

}
