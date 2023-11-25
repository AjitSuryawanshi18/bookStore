package com.bookStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookStore.entity.User;


@Repository
public interface UserRepo extends JpaRepository<User, Integer>{

	public User findByEmail(String email1);
}
