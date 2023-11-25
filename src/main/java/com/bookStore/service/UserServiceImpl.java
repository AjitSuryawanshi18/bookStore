package com.bookStore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.bookStore.entity.User;
import com.bookStore.repository.UserRepo;

import jakarta.servlet.http.HttpSession;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepo userRepo;

	@Override
	public User saveUser(User user) {
		User newUser=userRepo.save(user);
		return newUser;
	}

	// this is for remove attribute after refresh in spring boot 3 -->

	@Override
	public void removeSessionMessage() {
//		HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest()
//				.getSession();
		HttpSession httpSession=((ServletRequestAttributes)(RequestContextHolder.getRequestAttributes())).getRequest().getSession();

		httpSession.removeAttribute("msg");

	}



}
