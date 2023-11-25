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

import com.bookStore.entity.MyFav_Books;
import com.bookStore.entity.User;
import com.bookStore.entity.book;
import com.bookStore.repository.UserRepo;
import com.bookStore.service.BookService;
import com.bookStore.service.MyFav_BooksService;



@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	BookService service;


	@Autowired
	private UserRepo userRepo;

	@Autowired
	private MyFav_BooksService myBookService;


	@ModelAttribute
	public void commonUser(Principal p, Model m) {
		if (p != null) {
			String email = p.getName();
			User user = userRepo.findByEmail(email);
			m.addAttribute("user", user);
		}

	}

	@GetMapping("/profile")
	public String profile() {
		return "pages_From_RegistrationProject/profile";
	}

	@GetMapping("/home")
	public String home() {
		return "home";
	}

	// available books page controller method
			@GetMapping("/available_books")
			public ModelAndView available_books(Model model){
				model.addAttribute("activePage", "available_books");
				model.addAttribute("title","available_books - bookStore");

				List<book> list=service.available_books();
				return new ModelAndView("available_books","book",list);
			}

			// fav_books page controller method fav_books
			@GetMapping("/fav_books")
			public String fav_books(Model model){
				model.addAttribute("activePage", "fav_books");
				model.addAttribute("title","fav_books - bookStore");

				// for adding data to the favBooks after clicking add to Fav Books it will shows data is added or not on UI
				List<MyFav_Books>list=myBookService.getAllMyBooks();
				model.addAttribute("book",list);

				return "/fav_books";
			}
}
