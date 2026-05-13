package com.emile.fintech.accounts.model;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@Embeddable
public class AccountsId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String routingNo;
	private String accountNo;
	
	public AccountsId(String routingNo, String accountNo) {
		this.routingNo = routingNo;
		this.accountNo = accountNo;
	}
	
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
}
