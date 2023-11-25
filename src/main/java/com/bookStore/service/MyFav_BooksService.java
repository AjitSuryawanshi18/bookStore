package com.bookStore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookStore.entity.MyFav_Books;
import com.bookStore.repository.MyFav_BooksRepository;

@Service
public class MyFav_BooksService {

		@Autowired
		private MyFav_BooksRepository myfavbook;

		public void saveMyBooks(MyFav_Books book) {
			myfavbook.save(book);
		}

		public List<MyFav_Books> getAllMyBooks(){
			return myfavbook.findAll();
		}

		public void deleteById(int id) {
			myfavbook.deleteById(id);
		}

}
