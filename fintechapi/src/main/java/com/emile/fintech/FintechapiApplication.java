package com.emile.fintech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
//@ComponentScan(basePackages = { "com.emile.fintech.accounts.repos", "com.emile.fintech.user.repos", "com.emile.fintech.transactions.repos", "com.emile.fintech.accounts.controller", "com.emile.fintech.user.controller", "com.emile.fintech.transactions.controller", "com.emile.fintech.config"})
public class FintechapiApplication {
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(FintechapiApplication.class, args);
	}

}
