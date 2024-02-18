package com.bookStore.service;

import org.springframework.web.multipart.MultipartFile;

import com.bookStore.entity.User;

public interface UserService {

	public User saveUser(User user,String url);
	
	public User saveUser(User user);
	
	public void sendEmail(User user, String path);
	
	public void sendEmail_ForgotPassword(User user,String email, String path);
	
	public boolean verifyAccount(String verificationCode);

	public boolean deleteUserAccount(int user_Id);
	
	//profile image upload method
	public void uploadProfileImage(MultipartFile profileimageFile);
	
	
	
	// this is for remove attribute after refresh in spring boot 3 -->
	public void removeSessionMessage();

}
