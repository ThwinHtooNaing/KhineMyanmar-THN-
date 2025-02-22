package com.khineMyanmar.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.khineMyanmar.DTO.TopCustomerDTO;
import com.khineMyanmar.service.OrderService;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/top-customers")
    @ResponseBody
    public List<TopCustomerDTO> getTopCustomers() {
        return orderService.getTopCustomers();
    }
}
