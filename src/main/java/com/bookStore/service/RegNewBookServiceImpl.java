//package com.bookStore.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.bookStore.entity.book;
//import com.bookStore.repository.BookRepository;
//
//@Service
//public class RegNewBookServiceImpl implements RegNewBookService{
//	
//	@Autowired
//	private BookRepository bRepo;
//
//	@Override
//	public book saveNewBook(book b) {
//		
//		book newBook=bRepo.save(b);
//		
//		return newBook;
//	}
//
//}
