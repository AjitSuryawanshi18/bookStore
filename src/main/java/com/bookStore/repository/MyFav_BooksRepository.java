package com.bookStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookStore.entity.MyFav_Books;

@Repository
public interface MyFav_BooksRepository extends JpaRepository<MyFav_Books,Integer>{


}
