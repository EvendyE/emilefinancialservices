package com.emile.fintech.accounts.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.emile.fintech.accounts.repos.AccountsDataRepository;
//import com.emile.fintech.user.controller.UserController;

import jakarta.servlet.http.HttpSession;

import com.emile.fintech.accounts.dto.User;

@RestController // defines this class as a RESTful controller.
@RequestMapping("/accounts") // defines the URI path the controller should be mapped to.
//@CrossOrigin // gets the server and spring to permit the front-end (running on port 3000) and the back-end (running on port 8080) to communicate w/ each other.
public class AccountsRestController {

	@Autowired
	private AccountsDataRepository accountsRepository;
	
	@Autowired
	private RestTemplate restTemplate; /* allows requests to be made to other RESTful end points. RestTemplate must be
										* a method defined in the application class and annotated w/ the bean
										* annotation. */
	
	@RequestMapping(value = "/account", method = RequestMethod.POST) // @RequestBody allows submitted form values to be received as a RESTful API parameter.
	public List<Object> getAccount(@RequestBody String id) { // @RequestBody to bind the incoming JSON data to a Java object; otherwise, the java object will be null.	
		System.out.println("AccountsRestController");
		return accountsRepository.findByAccountId(Long.parseLong(id));
	}
	
//	@GetMapping("/callback")
//    public ResponseEntity<String> handleCallback(@RequestParam("code") String code) {
//        // Here you can use the authorization code to request an access token
//        return ResponseEntity.ok("Authorization code: " + code);
//    }
//	
//	@GetMapping("/csrf-token")
////	@ResponseBody // binds method return value to web response body.
//    public CsrfToken csrfToken(HttpSession session) {
//		CsrfToken token = (CsrfToken) session.getAttribute("_csrf");
//		Logger logger = LoggerFactory.getLogger(UserController.class);
//		logger.info("Retrieved CSRF Token: " + token.getToken());
//	    System.out.println("Retrieved CSRF Token: " + token.getToken());
//	    return token;
//    }
}
