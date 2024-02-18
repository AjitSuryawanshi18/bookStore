package com.bookStore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//third step  : to work with security i.e. when we use security it will provide default security if we want to mold it as per our requirements then we have to follow this steps :

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private CustomAuthSucccessHandler authSucccessHandler;

	//first step password encoder
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	//second step UserDetailsService
	@Bean
	public UserDetailsService getDetailsService() {
		return new CustomUserDetailsService();
	}

	//third step DaoAuthenticationProvider
	@Bean
	public DaoAuthenticationProvider getAuthenticationProvider() {

		DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider(); //object created of DaoAuthenticationProvider
		daoAuthenticationProvider.setUserDetailsService(getDetailsService()); //set userDatailsService
		daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());//set password


		return daoAuthenticationProvider; //return object

	}

	//forth step SecurityFilterChain
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		// work here

		//for now i am using deprecated methods for understanding concepts but after that we have to learn new approach for the same

		// this below code for learning basics
//		http.csrf().disable()
//		.authorizeHttpRequests().requestMatchers("/","/register","/signin","/saveUser").permitAll()
//		.requestMatchers("/user/**").authenticated().and()
//		.formLogin().loginPage("/signin").loginProcessingUrl("/userLogin")
//		.defaultSuccessUrl("/user/profile").permitAll();


		//actual role based authentication  code starts here

		http.csrf().disable()
		.authorizeHttpRequests().requestMatchers("/user/**").hasRole("USER")
		.requestMatchers("/admin/**").hasRole("ADMIN")
		.requestMatchers("/**").permitAll().and()
		.formLogin().loginPage("/signin").loginProcessingUrl("/userLogin")
		.successHandler(authSucccessHandler)
		.permitAll();




		return http.build();

	}
}
