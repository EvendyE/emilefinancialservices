package com.emile.fintech.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.emile.fintech.user.model.User;
import com.emile.fintech.user.repos.UserDataRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserDataRepository userRepository;
	
	@Autowired
    private HttpServletRequest request;

	/* custom implementation of UserDetails which only accepts a username as a parameter b/c the 
	 * password received from the form by Spring Security gets compared to the password corresponding 
	 * to the username, by spring security's default AuthenticationProvider when passed as parameters
	 * to the org.springframework.security.core.userdetails.User constructor. */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);		
		if (user == null) {
			throw new UsernameNotFoundException("User not found for email" + username);
		}
		System.out.println("UserDetailsServiceImpl: " + user.getUsername() + "\n" + user.getPassword());
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				user.getRoles());
	}

}