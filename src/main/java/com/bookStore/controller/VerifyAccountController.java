package com.bookStore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bookStore.entity.User;
import com.bookStore.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class VerifyAccountController {
	
	@Autowired
	private UserService userService;

	
//	code start for verify account or enable account
	
	
//	mapping for save user
	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute User user,HttpSession session,Model m,HttpServletRequest request) {

//		 System.out.println(user);
		
		
		String url = request.getRequestURL().toString();
//		System.out.println(url);  //   http://localhost:1001/saveUser
		url = url.replace(request.getServletPath(), "");
//		System.out.println(url);  //   http://localhost:1001
		
		//we want like we will attach remaining url dynamically    http://localhost:8080/verify?code=3453sdfsdcsadcscd
		
        
		User u = userService.saveUser(user,url);

		if (u != null) {

			session.setAttribute("msg","Register Successfully Please Check Your Email For Verification");

		} else {

			session.setAttribute("msg","Something wrong server");
		}
		return "redirect:/register";
	}

	
//	        is for verify account after mail send 
	@GetMapping("/verify")
	public String verifyAccount(@RequestParam("code") String code,Model model) {
		
		boolean userVerified = userService.verifyAccount(code);
		
		if (userVerified) {
			
			model.addAttribute("msg", "Yahoo Account Verified Succcessfully Go Explore Book's Now...!!");
//			System.out.println("verified succesfully");
			
		} else {
			
			model.addAttribute("msgF", "Wrong Code or Account Verified Already...!!");
//			System.out.println("already verified");

		}
		
		return "/emailRelated/messages";
	}

	
	
	

	
	
	
}
