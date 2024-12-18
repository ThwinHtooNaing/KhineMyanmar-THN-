package com.khineMyanmar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ecom")
public class StartController {
	@GetMapping("/index")
	public String start() {
		return "index";//page name
	}
}
