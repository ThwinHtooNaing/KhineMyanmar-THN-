package com.khineMyanmar.controller;
import com.khineMyanmar.model.Category;
import com.khineMyanmar.model.User;
import com.khineMyanmar.service.CategoryService;

import org.springframework.ui.Model;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CategoryService categoryService;

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
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
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

    @PostMapping("/update")
    @ResponseBody
    public String updateCategory(@RequestParam Long id, @RequestParam String name) {
        Category category = categoryService.getCategoryById(id);
        if (category != null) {
            category.setCategoryName(name);
            categoryService.saveCategory(category);
            return "Updated Successfully";
        }
        return "Category Not Found";
    }

    // Delete Category (AJAX call)
    @PostMapping("/delete")
    @ResponseBody
    public String deleteCategory(@RequestParam Long id) {
        categoryService.deleteCategory(id);
        return "Deleted Successfully";
    }

    

}
