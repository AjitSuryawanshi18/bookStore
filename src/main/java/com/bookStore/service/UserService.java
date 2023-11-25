package com.bookStore.service;

import com.bookStore.entity.User;

public interface UserService {

	public User saveUser(User user);

	// this is for remove attribute after refresh in spring boot 3 -->
	public void removeSessionMessage();

}
