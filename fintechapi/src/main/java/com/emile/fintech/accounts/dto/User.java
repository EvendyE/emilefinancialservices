package com.emile.fintech.accounts.dto;

/* dto serves as the data type that the incoming JSON response of the request 
 * made to a RESTful end point, will be deserialized to. The class properties 
 * are what the response will be modeled after. */
public class User {
	
	private Long id;

	private String username;

	private String password;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
