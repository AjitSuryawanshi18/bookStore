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

import com.bookStore.entity.MyFav_Books;
import com.bookStore.entity.User;
import com.bookStore.entity.book;
import com.bookStore.repository.UserRepo;
import com.bookStore.service.BookService;
import com.bookStore.service.MyFav_BooksService;
import com.bookStore.service.UserService;

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
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserRepo userRepo;


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

	// available books page controller method added in admin controller
//	@GetMapping("/available_books")
//	public ModelAndView available_books(Model model){
//		model.addAttribute("activePage", "available_books");
//		model.addAttribute("title","available_books - bookStore");
//
//		List<book> list=service.available_books();
//		return new ModelAndView("available_books","book",list);
//	}

	// controller for addBook or save book from admin login request come here to store new registered book
		@PostMapping("/save")
		public String addBook(@ModelAttribute book b) {
			service.save(b);
			return "redirect:/available_books";
		}


//	// fav_books page controller method fav_books  added to user controller for user profile access only
//	@GetMapping("/fav_books")
//	public String fav_books(Model model){
//		model.addAttribute("activePage", "fav_books");
//		model.addAttribute("title","fav_books - bookStore");
//
//		// for adding data to the favBooks after clicking add to Fav Books it will shows data is added or not on UI
//		List<MyFav_Books>list=myBookService.getAllMyBooks();
//		model.addAttribute("book",list);
//
//		return "/fav_books";
//	}


	// controller for getting book which have to add to fav column with reference to  its id
	@RequestMapping("/mylist/{id}")
	public String getMyList(@PathVariable("id") int id) {
		book b=service.getBookById(id);
		MyFav_Books mb=new MyFav_Books(b.getId(),b.getName(),b.getAuthor(),b.getPrice());
		myBookService.saveMyBooks(mb);
		return "redirect:/fav_books";
	}


//	controller for delete book
	@GetMapping("/deleteBook/{id}")
	public String deleteBook(@PathVariable("id") int id) {
		service.deleteById(id);
		return "redirect:/available_books";
	}

	//controller for edit book
	@GetMapping("/available_books/editBook/{id}")
	public String editBook(@PathVariable("id") int id,Model model) {
		book b=service.getBookById(id);
		model.addAttribute("book", b);
		return "editBook";
	}







//	// register new book controller method
//	@GetMapping("/register_newbook")
//	public String register_newbook(Model model){
//		model.addAttribute("activePage", "register_newbook");
//		model.addAttribute("title","register_newbook - bookStore");
//
//		return "/register_newbook";
//	}






	// for easy access to all like user == null or more...

	@ModelAttribute
	public void commonUser(Principal p, Model m) {

		if (p != null) {
			String email = p.getName();
			User user = userRepo.findByEmail(email);
			m.addAttribute("user", user);
		}

	}




	//this is save user from RegLoginController moved here bcz here also have direct access like RegLoginController


//	controller for save user
	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute User user,HttpSession session,Model m) {

//		 System.out.println(user);
        String password=bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(password);
        user.setRole("ROLE_USER");
		User u = userService.saveUser(user);

		if (u != null) {

			session.setAttribute("msg", "Register successfully");

		} else {

			session.setAttribute("msg", "Something wrong server");
		}
		return "redirect:/register";
	}


}
