package com.bookStore.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bookStore.entity.MyFav_Books;
import com.bookStore.entity.User;
import com.bookStore.entity.book;
import com.bookStore.repository.UserRepository;
import com.bookStore.service.BookService;
import com.bookStore.service.MyFav_BooksService;
import com.bookStore.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;



@Controller
public class bookController {
	@Autowired
	BookService service;

	@Autowired
	private MyFav_BooksService myBookService;


	@Autowired
	private UserService userService;


	@Autowired
	private UserRepository userRepository;


	// home controller method
	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("activePage", "home");
		model.addAttribute("title","Home - bookStore");

		return "/home";
	}

	// about page controller method
	@GetMapping("/about")
	public String about(Model model){
		model.addAttribute("activePage", "about");
		model.addAttribute("title","about - bookStore");

		return "/about";
	}

	// contact page controller method
	@GetMapping("/contact")
	public String contact(Model model){
		model.addAttribute("activePage", "contact");
		model.addAttribute("title","contact - bookStore");

		return "/contact";
	}

	// link page controller method
	@GetMapping("/link")
	public String link(Model model){
		model.addAttribute("activePage", "link");
		model.addAttribute("title","link - bookStore");
		return "/link";

	}

//	this is for new user to register first time at our website
	@GetMapping("/register")
	public String register() {
		return "pages_From_RegistrationProject/register";
	}

//	this is for user to login or admin to login
	@GetMapping("/signin")
	public String signin() {
		return "pages_From_RegistrationProject/signin";
	}
	





	// controller for getting book which have to add to fav column with reference to  its id
	@RequestMapping("/mylist/{id}")
	public String getMyList(@PathVariable("id") int id) {
		book b=service.getBookById(id);
		MyFav_Books mb=new MyFav_Books(b.getId(),b.getName(),b.getAuthor(),b.getPrice());
		myBookService.saveMyBooks(mb);
		return "redirect:/fav_books";
	}





	// for easy access to all like user == null or more...

	@ModelAttribute
	public void commonUser(Principal p, Model m) {

		if (p != null) {
			String email = p.getName();
			User user = userRepository.findByEmail(email);
			m.addAttribute("user", user);
		}

	}
	



	
}
