package com.emile.fintech.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class SecurityServiceImpl implements SecurityService {

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	SecurityContextRepository securityContextRepo;
	
	@Override
	public boolean login(String userName, String password, HttpServletRequest request, HttpServletResponse response) { // the parameters are possibly received from the default login endpoint.
		UserDetails userDetails = userDetailsService.loadUserByUsername(userName); // userName is passed to an instance of a repository in the userDetailsService class, to be queried against a table in the database.
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities()); // an instance of UsernamePasswordAuthenticationToken takes all the details required for authentication.
		authenticationManager.authenticate(token); // upon successful authentication, authenticationManager updates the token's authentication status to true.
		boolean result = token.isAuthenticated(); // true if authenticated, false if not.
		
		if(result) {
			SecurityContext context = SecurityContextHolder.getContext();
			context.setAuthentication(token); // saves username and password
			securityContextRepo.saveContext(context, request, response);
		}
		return result;
	}

}
