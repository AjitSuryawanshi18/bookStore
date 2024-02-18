package com.bookStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookStore.entity.ForgotPasswordToken;


@Repository
public interface ForgotPasswordRepository extends JpaRepository<ForgotPasswordToken, Long> {
	
	ForgotPasswordToken findByToken(String token);

}
