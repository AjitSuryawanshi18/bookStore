package com.bookStore.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bookStore.entity.ForgotPasswordToken;
import com.bookStore.entity.User;
import com.bookStore.repository.ForgotPasswordRepository;
import com.bookStore.repository.UserRepository;
import com.bookStore.service.ForgotPasswordService;
import com.bookStore.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class ForgotPasswordController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ForgotPasswordService forgotPasswordService;
	
	@Autowired
	ForgotPasswordRepository forgotPasswordRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	
//	below code is coding till the mail sent and after that code for after sending mail
	
	@GetMapping("/password-request")
	public String passwordRequest() {
	
		return "/ForgotPassword/password-request";
	}
	
	
	
	@PostMapping("/password-request")
	public String savePasswordRequest(@RequestParam("email") String email, Model model,HttpServletRequest request) {
		User user = userRepository.findByEmail(email);
		if (user == null) {
			model.addAttribute("error", "This Email is not registered");
			return "/ForgotPassword/password-request";
		}
		
		//token entity class object so we can setting data to the token entity.
		
		ForgotPasswordToken forgotPasswordToken = new ForgotPasswordToken();
		forgotPasswordToken.setExpireTime(forgotPasswordService.expireTimeRange());
		forgotPasswordToken.setToken(forgotPasswordService.generateToken());
		forgotPasswordToken.setUser(user);
		forgotPasswordToken.setUsed(false);
		
		forgotPasswordRepository.save(forgotPasswordToken); //it will save all the token data to the database i.e. forgot_password_token entity table
		
//		configuration of the path url dynamically
		String url = request.getRequestURL().toString();
//		System.out.println(url);  //   http://localhost:1001/password-request
		url = url.replace(request.getServletPath(), "");
//		System.out.println(url);  //   http://localhost:1001
				
		String emailLink = url +"/reset-password?token=" + forgotPasswordToken.getToken();
		
//getting user name who want to forgot their password and it will be set in email like "hello Dear User_Name"
		String User_Name = user.getName();
		
		try {
			forgotPasswordService.sendEmail(user.getEmail(),"Password Reset Link", emailLink,User_Name);
		} catch (UnsupportedEncodingException | MessagingException e) {
			model.addAttribute("error", "Error While Sending email");
			return "/ForgotPassword/password-request";
		}
		
		
		return "redirect:/password-request?success";
	}
	

	
	
	
	
	
	
	
//	from below here is the code which running after the mail or we can say from link which provided in the mail itself.
	
	
	@GetMapping("/reset-password")
	public String resetPassword(@Param(value="token") String token, Model model, HttpSession session) {
		
		session.setAttribute("token", token);
		ForgotPasswordToken forgotPasswordToken = forgotPasswordRepository.findByToken(token);
		return forgotPasswordService.checkValidity(forgotPasswordToken, model);
		
	}
	
	@PostMapping("/reset-password")
	public String saveResetPassword(HttpServletRequest request, HttpSession session, Model model) {
		String password = request.getParameter("password");
		String token = (String)session.getAttribute("token");
		
		ForgotPasswordToken forgotPasswordToken = forgotPasswordRepository.findByToken(token);
		User user = forgotPasswordToken.getUser();
		user.setPassword(passwordEncoder.encode(password));
		forgotPasswordToken.setUsed(true);
		userService.saveUser(user);
		forgotPasswordRepository.save(forgotPasswordToken);
		
		model.addAttribute("message", "You have successfuly reset your password Login Now!!");
		
		return "/ForgotPassword/reset-password";
	}

}
