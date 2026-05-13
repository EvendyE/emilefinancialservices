package com.emile.fintech.transactions.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.emile.fintech.transactions.repos.TransactionsDataRepository;

@RestController // defines this class as a RESTful controller.
@RequestMapping("/transactions") // defines the URI path the controller should be mapped to.
//@CrossOrigin // gets the server and spring to permit the front-end (running on port 3000) and the back-end (running on port 8080) to communicate w/ each other.
public class TransactionsRestController {

	@Autowired
	private TransactionsDataRepository transactionsRepository;
	
	@Autowired
	private RestTemplate restTemplate; /* Allows requests to be made to other RESTful end points. RestTemplate must be
										  a method defined in the application class and annotated w/ the bean
										  annotation. */
}
