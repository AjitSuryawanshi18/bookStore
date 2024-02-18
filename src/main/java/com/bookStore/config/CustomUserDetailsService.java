package com.bookStore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.bookStore.entity.User;
import com.bookStore.repository.UserRepository;





//Second step  : to work with security i.e. when we use security it will provide default security if we want to mold it as per our requirements then we have to follow this steps :

public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user=userRepository.findByEmail(username);

//		System.out.println(user); // printing for checking purpose

		if (user == null) {
			throw new UsernameNotFoundException("user not found");
		} else {
			return new CustomUser(user);
		}
	}



}
