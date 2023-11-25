package com.bookStore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.bookStore.entity.book;
import com.bookStore.service.BookService;

@Controller
public class BuyNowController {

	@Autowired
	BookService service;

//	buyNow Page controller
	@GetMapping("/buyNow/{id}")
	public String buyNow(@PathVariable("id") int id,Model model) {
		book b=service.getBookById(id);
		model.addAttribute("book", b);
		return "/buyNow";
	}


	// confirmation message
	@GetMapping("/buyNow/congrats_Message")
     public String congrats_Message(Model model) {
		model.addAttribute("title", "Confirmation Message");
    	 return "congrats_Message";
     }
}
