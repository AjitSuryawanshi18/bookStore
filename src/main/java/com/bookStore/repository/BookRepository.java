package com.bookStore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookStore.entity.book;

@Repository
public interface BookRepository extends JpaRepository<book,Integer>  {
    // Custom queries, if needed
	
	List<book> findByBookType(String bookType);

}
