package com.khineMyanmar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.khineMyanmar.model.Order;
import com.khineMyanmar.model.OrderStatus;
import com.khineMyanmar.service.OrderService;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @ResponseBody
    @GetMapping("/getOrders")
    public Page<Order> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status) {  // Accept String as status
        Pageable pageable = PageRequest.of(page, size);
        System.out.println(status);
        if (status == null || "ALL".equalsIgnoreCase(status)) {
            System.out.println("Fetching all orders");
            return orderService.getAllOrders(pageable);  // Fetch all orders
        }
        
        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            System.out.println(orderStatus.toString());
            return orderService.getOrders(orderStatus, pageable);
        } catch (IllegalArgumentException e) {
            return orderService.getAllOrders(pageable); // If status is invalid, fetch all orders
        }
    }
}
