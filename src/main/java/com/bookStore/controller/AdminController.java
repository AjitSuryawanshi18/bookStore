package com.bookStore.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bookStore.entity.User;
import com.bookStore.entity.book;
import com.bookStore.repository.UserRepo;
import com.bookStore.service.BookService;



@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	BookService service;


	@Autowired
	private UserRepo userRepo;

	@ModelAttribute
	public void commonUser(Principal p, Model m) {
		if (p != null) {
			String email = p.getName();
			User adminUser = userRepo.findByEmail(email);
			m.addAttribute("user", adminUser);
		}
	}


	@GetMapping("/profile")
	public String profile() {
		return "pages_From_RegistrationProject/admin_profile";
	}
	@GetMapping("/home")
	public String home() {
		return "home";
	}

	// register new book controller method
		@GetMapping("/register_newbook")
		public String register_newbook(Model model){
			model.addAttribute("activePage", "register_newbook");
			model.addAttribute("title","register_newbook - bookStore");

			return "/register_newbook";
		}

		// available books page controller method
		@GetMapping("/available_books")
		public ModelAndView available_books(Model model){
			model.addAttribute("activePage", "available_books");
			model.addAttribute("title","available_books - bookStore");

			List<book> list=service.available_books();
			return new ModelAndView("available_books","book",list);
		}

}
