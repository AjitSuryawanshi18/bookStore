package com.bookStore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookStore.entity.book;
import com.bookStore.repository.BookRepository;

@Service
public class BookService {

	@Autowired
	private BookRepository bRepo;

	// service for save method is implemented in RegNewBookService for successful message
//	public void save(book b) {
//		bRepo.save(b);
//	}
	
	public book saveBook(book book) {
       return bRepo.save(book);
    }
	
	//service for show all available books
	public List<book> getAllBooks(){
		return bRepo.findAll();
	}
	//service for show available books with new category
//	public List<book> getBooksByType(){
//		return bRepo.findAll();
//	}
	
	    public List<book> getBooksByType(String bookType) {
	        return bRepo.findByBookType(bookType);
	    }

	public book getBookById(int id) {
		return bRepo.findById(id).get();
	}
	public void deleteById(int id) {
		bRepo.deleteById(id);
	}
}