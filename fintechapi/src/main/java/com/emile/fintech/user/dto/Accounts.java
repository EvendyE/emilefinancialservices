package com.emile.fintech.user.dto;

public class Accounts {
	
	/* dto serves as the data type that the incoming JSON response of the request 
	 * made to a RESTful end point, will be deserialized to. The class properties 
	 * are what the response will be modeled after. */
	private String routingNo;

	private String accountNo;

	private float balance;

	private String accType;

	public String getRoutingNo() {
		return routingNo;
	}

	public void setRoutingNo(String routingNo) {
		this.routingNo = routingNo;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	public String getAccType() {
		return accType;
	}

	public void setAccType(String accType) {
		this.accType = accType;
	}

}
