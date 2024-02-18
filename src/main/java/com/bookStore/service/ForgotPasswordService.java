package com.bookStore.service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.bookStore.entity.ForgotPasswordToken;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class ForgotPasswordService {


	@Autowired
	private JavaMailSender javaMailSender;
	
    private final int MINUTES = 10;
	
	public String generateToken() {
		return UUID.randomUUID().toString();
	}
	
	public LocalDateTime expireTimeRange() {
		return LocalDateTime.now().plusMinutes(MINUTES);
	}
	
	public void sendEmail(String to, String subject, String emailLink,String name) throws MessagingException, UnsupportedEncodingException {
		
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		String emailContent = "<p>Hello Dear [[name]],</p>"
				              + "Click the link below to reset your password"
				              + "<p><a href=\""+ emailLink + "\">Change My Password</a></p>"
				              + "<br>"
				              + "Ignore this Email if you did not made the request";
		
		
		emailContent = emailContent.replace("[[name]]", name);

		helper.setText(emailContent, true);
		helper.setFrom("DevGuru1845@gmail.com", "BookStore Support");
		helper.setSubject(subject);
		helper.setTo(to);
		javaMailSender.send(message);
	}
	
	
	public boolean isExpired (ForgotPasswordToken forgotPasswordToken) {
		return LocalDateTime.now().isAfter(forgotPasswordToken.getExpireTime());
	}
	
	public String checkValidity (ForgotPasswordToken forgotPasswordToken, Model model) {
		
		if (forgotPasswordToken == null) {
			model.addAttribute("error", "Invalid Link Please Check Again");
			return "/ForgotPassword/error-page";
		}
		
		else if (forgotPasswordToken.isUsed()) {
			model.addAttribute("error", "Oops The Link Is already used");
			return "/ForgotPassword/error-page";
		}
		
		else if (isExpired(forgotPasswordToken)) {
			model.addAttribute("error", "ohoo The Link Is Expired");
			return "/ForgotPassword/error-page";
		}
		else {
			return "/ForgotPassword/reset-password";
		}
		
		
	}
}
