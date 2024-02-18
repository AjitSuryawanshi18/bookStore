package com.bookStore.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bookStore.entity.User;
import com.bookStore.entity.book;
import com.bookStore.repository.UserRepository;
import com.bookStore.service.BookService;

@Controller
@RequestMapping("/user")
public class BuyNowController {
	
	@Autowired
	private UserRepository userRepository;
	
	// for user object to the templates
	@ModelAttribute
	public void commonUser(Principal p, Model m) {
		if (p != null) {
			String email = p.getName();
			User user = userRepository.findByEmail(email);
			m.addAttribute("user", user);
		}

	}
	
	
	// for new book buy start

	@Autowired
	BookService service;
//	buyNow Page controller
	@GetMapping("/buyNow/{id}")
	public String buyNow(@PathVariable("id") int id, Model model) {
		book b = service.getBookById(id);
		model.addAttribute("book", b);
		return "/buyNow";
	}

	// confirmation message
	@GetMapping("/buyNow/congrats_Message")
	public String congrats_Message(Model model) {
		model.addAttribute("title", "Confirmation Message");
		return "congrats_Message";
	}
	
	
	// for new book buy end
	
	
	
	
	
//	view details of particular book start .....
	
	@GetMapping("/available_NewBooks/view-details/{id}")
	public ModelAndView viewDetails(Model model,@PathVariable("id") int id) {
		 
		model.addAttribute("activePage", "available_newBooks");
		model.addAttribute("title", "ViewDetails - bookStore");

		book bookToViewDetails = service.getBookById(id);
		return new ModelAndView("/userProfile/view-details", "book", bookToViewDetails);
			
	}
	
	
	@GetMapping("/available_ResaleBooks/view-details_Resale/{id}")
	public ModelAndView viewDetails_Resale(Model model,@PathVariable("id") int id) {
		
		model.addAttribute("activePage", "available_ResaleBooks");
		model.addAttribute("title", "view-details_Resale - bookStore");
		
		book bookToViewDetails = service.getBookById(id);
		return new ModelAndView("/userProfile/view-details_Resale", "book", bookToViewDetails);
		
	}
	
	

//	view details of particular book end .....
	
	
	
	
	
	
	// for Resale book request start
	
	
	@GetMapping("/available_ResaleBooks/view-details_Resale/View_Summary/{id}")
	public ModelAndView View_Summary(Model model,@PathVariable("id") int id) {
		
		model.addAttribute("activePage", "View_Summary");
		model.addAttribute("title", "View_Summary - bookStore");
		
		book bookToViewDetails = service.getBookById(id);
		return new ModelAndView("/View_Summary", "book", bookToViewDetails);
		
	}
	
	// Requested message
		@GetMapping("/available_ResaleBooks/view-details_Resale/View_Summary/Requested_Message")
		public String congrats_Message_AfterBookRequest(Model model) {
			model.addAttribute("title", "Requested Message");
			return "Requested_Message";
		}
		
	
	// for Resale book request end




}
