package com.bookStore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bookStore.service.MyFav_BooksService;

@Controller
public class MyFav_BooksController {

		@Autowired
		private MyFav_BooksService service;

		// this for delete favorite book
		@RequestMapping("/deleteMyList/{id}")
		public String deleteMyList(@PathVariable("id") int id) {
			service.deleteById(id);
			return "redirect:/fav_books";
		}

}
