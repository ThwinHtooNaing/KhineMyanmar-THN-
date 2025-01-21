package com.khineMyanmar.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chart")
public class ChartController {

    @GetMapping("/sales-category")
    public Map<String, Object> getSalesCategoryData() {
        Map<String, Object> data = new HashMap<>();
        data.put("series", List.of(50, 30, 15, 5)); // Example sales data for categories
        data.put("labels", List.of("Electronics", "Fashion", "Home & Kitchen", "Others")); // Example categories
        return data;
    }

    
}

