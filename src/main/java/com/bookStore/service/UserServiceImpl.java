package com.bookStore.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.bookStore.entity.User;
import com.bookStore.repository.UserRepository;

import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	
//	this saveUser is for saving password change of an user and admin etc 
	@Override
	public User saveUser(User user) {
		User newUser=userRepository.save(user);
		return newUser;
	}
	

	//this saveUser is for registration of an new user and then verify their account 
	@Override
	public User saveUser(User user,String url) {
		
		 String password=bCryptPasswordEncoder.encode(user.getPassword());
	        user.setPassword(password);
	        user.setRole("ROLE_USER");
		
	        
		user.setEnable(false);
		user.setVerificationCode(UUID.randomUUID().toString());
		
		
		
		User newUser = userRepository.save(user);
		
		if(newUser != null) {
			sendEmail(newUser, url);
		}
		// think for corner case here and possible solve it or let it be will check after 
		return newUser;
	}


	
	
	// method for verification of user email
	@Override
	public void sendEmail(User user, String url) {

		String from = "devguru1845@gmail.com";
//		String from = "DevGuru1845@gmail.com";
		String to = user.getEmail();
		String subject = "Account Verfication";
		String content = "Dear [[name]],<br>" + "Please click the link below to verify your registration:<br>"
				+ "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>" + "Thank you,<br>" + "BookStore";

		try {

			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);

			helper.setFrom(from, "BookStore");
			helper.setTo(to);
			helper.setSubject(subject);

			content = content.replace("[[name]]", user.getName());
			String siteUrl = url + "/verify?code=" + user.getVerificationCode();

			System.out.println(siteUrl);

			content = content.replace("[[URL]]", siteUrl);

			helper.setText(content, true);

			mailSender.send(message);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}



//method for verification of user account
	@Override
	public boolean verifyAccount(String verificationCode) {
		
		User user = userRepository.findByVerificationCode(verificationCode);
		
		if (user == null) {
			return false;
		} else {	
		
			user.setEnable(true);
			user.setVerificationCode(null);
			
			userRepository.save(user);
			return true;
		}
		
	}


	
	// method for send forgot password link via email
	@Override
	public void sendEmail_ForgotPassword(User user,String email, String url) {

		System.out.println("sendEmail_ForgotPassword called");

		String from = "devguru1845@gmail.com";
//		String from = "DevGuru1845@gmail.com";

		String to = email;
		String subject = "Forgot Account Password";
		String content = "Dear Abc [[name]],<br>" + "Please click the link below to reset your password:<br>"
				+ "<h3><a href=\"[[URL]]\" target=\"_self\">Forgot Password</a></h3>" + "Thank you,<br>" + "BookStore";

		try {

			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);

			helper.setFrom(from, "BookStore");
			helper.setTo(to);
			helper.setSubject(subject);

			System.out.println("before name replacement");
			
//			content = content.replace("[[name]]", user.getName());
			
//			System.out.println("after name replacement" +user.getName());
			
			String siteUrl = url + "/forgotPassfromMail";

			System.out.println(siteUrl);

			content = content.replace("[[URL]]", siteUrl);

			helper.setText(content, true);

			mailSender.send(message);

		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}
	
	
	
	// this is for remove attribute after refresh in spring boot 3 -->

		@Override
		public void removeSessionMessage() {

			HttpSession httpSession=((ServletRequestAttributes)(RequestContextHolder.getRequestAttributes())).getRequest().getSession();

			//here we add each message name which we set in session it can removed after refresh
			httpSession.removeAttribute("msg");
			httpSession.removeAttribute("msg_red");
			httpSession.removeAttribute("bookPresent");

		}


		
//		profile image upload 
		
		@Override
		public void uploadProfileImage(MultipartFile profileimageFile) {
			// TODO Auto-generated method stub
			
		}


		
		
		
		//delete user account permanent
		@Override
		public boolean deleteUserAccount(int userId) {
		
		
		        User user = userRepository.findById(userId).orElse(null);

		        if (user != null) {
		        	// Delete the profile image		        	
		        	String profileImageName = user.getProfileImage();
		        	
		        	deleteProfileImage(profileImageName);
					
		            // Delete user-related data from the database
		            userRepository.deleteById(userId);
		            
		            return true;
		        }else {
		        	return false;
		        }
		    
		}

//delete user profile image
		private void deleteProfileImage(String profileImageName) {
	        try {
	            // Use appropriate file system interaction to delete the image file
	        	
	        	String directoryPath = "C:\\Users\\ajits\\Documents\\workspace-spring-tool-suite-4-4.19.0.RELEASE\\BOOKSTORE_PROJECT\\bookStore/src/main/resources/static/images/UserProfile_Photos/";

				Path imagePath = Paths.get(directoryPath, profileImageName);

	            Files.deleteIfExists(imagePath);
//	            System.out.println("Profile image deleted: " + imagePath);
	        } catch (IOException e) {
	            // Handle file deletion errors
//	            System.err.println("Error deleting profile image: " + e.getMessage());
	        }
	    }



	





}
