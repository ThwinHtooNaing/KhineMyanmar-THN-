package com.khineMyanmar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.khineMyanmar.service.OrderService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/chart")
@Controller
public class ChartController {

    @Autowired
    private OrderService orderService; 

    @GetMapping("/weekly-orders")
    @ResponseBody
    public Map<String, Object> getWeeklyOrders() {
        System.out.println("getWeeklyOrders");
        return orderService.getWeeklyOrderStats();
    }

    @GetMapping("/sales-category")
    @ResponseBody
    public Map<String, Object> getSalesCategoryData() {
        Map<String, Object> data = new HashMap<>();
        data.put("series", List.of(50, 30, 15, 5)); // Example sales data for categories
        data.put("labels", List.of("Electronics", "Fashion", "Home & Kitchen", "Others")); // Example categories
        return data;
    }

   

    
}

