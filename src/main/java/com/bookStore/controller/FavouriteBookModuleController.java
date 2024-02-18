package com.bookStore.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.bookStore.entity.User;
import com.bookStore.entity.book;
import com.bookStore.repository.BookRepository;
import com.bookStore.repository.UserRepository;
import com.bookStore.service.BookService;

import jakarta.servlet.http.HttpSession;

@Controller
public class FavouriteBookModuleController {
	
	@Autowired
	private BookService service;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private UserRepository userRepository;

	// mapping for empty fav Books Column
	@GetMapping("/FavBookModule/empty")
	public String empty() {
		return "/FavBookModule/empty";
	}

	@GetMapping("/user/FavBookModule/favourite-books")
	public String viewFavorites(Principal p, Model model) {
		
		model.addAttribute("activePage", "favourite-books");
		
		model.addAttribute("title", "Favourite Books - BookStore");
		// Retrieve user and their favorite books
		String name = p.getName();
		User user = userRepository.findByEmail(name);
		List<book> favoriteBooks = user.getFavoriteBooks();

//	        checking favorite area is empty or not
		boolean b = user.getFavoriteBooks().isEmpty();
		if (b) {
			model.addAttribute("emptyMsg", "Your Favourite Book List Is Empty Please Add Book...!");
			model.addAttribute("user", user);
			return "/FavBookModule/empty";
		}

		// Add data to the model for Thymeleaf
		model.addAttribute("user", user);
		model.addAttribute("favbooks", favoriteBooks);

		return "/FavBookModule/favourite-books";
	}

	@PostMapping("/user/addBook/{bookId}")
	public String addBookToFavorite(Principal p, @PathVariable Integer bookId, HttpSession session) {

		String name = p.getName();
		User user = userRepository.findByEmail(name);
//		int userId = user.getId();

		book book = bookRepository.findById(bookId).orElseThrow();

//	     System.out.println("book present id is: "+ user.getFavoriteBooks().contains(book));

	     if(user.getFavoriteBooks().contains(book)) {
	    	 session.setAttribute("bookPresent", "This Book is Already In Your Favourite List!!");
	    	 return "redirect:/user/FavBookModule/favourite-books";
	     }
	     
		user.getFavoriteBooks().add(book);
		userRepository.save(user);
		return "redirect:/user/FavBookModule/favourite-books";
	}
	
	
	
	
	
	

	@PostMapping("/user/removeBook/{bookId}")
	public String removeBookFromFavorite(Principal p, @PathVariable Integer bookId, Model model) {
		String name = p.getName();
		User user = userRepository.findByEmail(name);

//		System.out.println("it is an user id from remove book method : " + user.getId());
		
		book book = bookRepository.findById(bookId).orElseThrow();
		user.getFavoriteBooks().remove(book);
		userRepository.save(user);

//        checking favorite area is empty or not
		boolean b = user.getFavoriteBooks().isEmpty();
		if (b) {
			model.addAttribute("emptyMsg", "Oops You Removed Your All Favourite Book's List...!");
			model.addAttribute("user", user);
			return "/FavBookModule/empty";
		}

		
		return "redirect:/user/FavBookModule/favourite-books";
	}

}
