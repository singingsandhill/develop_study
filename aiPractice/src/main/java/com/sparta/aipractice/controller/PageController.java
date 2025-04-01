package com.sparta.aipractice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
	@GetMapping("/")
	public String index() {
		return "index"; // resources/templates/index.html
	}
}
