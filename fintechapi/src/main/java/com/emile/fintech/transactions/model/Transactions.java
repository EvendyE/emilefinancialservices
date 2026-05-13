package com.emile.fintech.transactions.model;

import java.sql.Timestamp;

import com.emile.fintech.accounts.model.Accounts;
import com.emile.fintech.accounts.model.AccountsId;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

/*@Embeddable /* made embeddable so it's instance variables marked w/ the embedded annotation
			   may be mapped to columns of the database table which the Transaction entity
			   is mapped to. */
			  /* note: for whatever reason, a class can't be marked w/ an entity annotation
				 and an embeddable annotation */
@Entity // maps class to database table.
public class Transactions {
	
	@EmbeddedId
	@AttributeOverrides({
	    @AttributeOverride(name = "routingNo", column = @Column(name = "routing_number")),
	    @AttributeOverride(name = "accountNo", column = @Column(name = "account_number"))
	    })
	private AccountsId compositeKey;
	
	@Column(name="vendor_id")
	private String vendorId; // the identifier that will appear on the banking statement.
	
	@Column(name="auto_pay")
	private boolean autoPay; /* checks whether automatic payments was requested by a vendor, as authorized by
								their customer. Based on vendorId from Transaction microservice. */
	
	private float deposit; // refunds, direct/check deposits, etc...
	
	private float withdrawal; // purchases, atm/bank withdrawals, automatic payments, etc...
	
	private String ipv6;
	
	private String ipv4;
	
	@Column(name="date_time")
	private Timestamp dateTime; /* date and time of when the purchase was made. Will be shared w/ Accounts
							  microservice to determine when it will be processed. */

//	@Embedded // frozen is embedded from the Accounts entity.
	@Column(name="freeze")
	private Boolean frozen; /* before making a purchase, a request is made to the Account microservice on
							   whether or not the debit card is frozen. */
	
//	@Embedded // missing is embedded from the Accounts entity. 
	private Boolean missing; /* send request to Accounts microservice about whether the card is lost or
								stolen before accepting a purchase. */

	public AccountsId getCompositeKey() {
		return compositeKey;
	}
	
	public void setCompositeKey(AccountsId compositeKey) {
		this.compositeKey = compositeKey;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public boolean isAutoPay() {
		return autoPay;
	}

	public void setAutoPay(boolean autoPay) {
		this.autoPay = autoPay;
	}

	public float getDeposit() {
		return deposit;
	}

	public void setDeposit(float deposit) {
		this.deposit = deposit;
	}

	public float getWithdrawal() {
		return withdrawal;
	}

	public void setWithdrawal(float withdrawal) {
		this.withdrawal = withdrawal;
	}

	public String getIpv6() {
		return ipv6;
	}

	public void setIpv6(String ipv6) {
		this.ipv6 = ipv6;
	}

	public String getIpv4() {
		return ipv4;
	}

	public void setIpv4(String ipv4) {
		this.ipv4 = ipv4;
	}

	public Timestamp getDateTime() {
		return dateTime;
	}

	public void setDateTime(Timestamp dateTime) {
		this.dateTime = dateTime;
	}

	public Boolean isFrozen() {
		return frozen;
	}

	public void setFrozen(Boolean frozen) {
		this.frozen = frozen;
	}

	public boolean isMissing() {
		return missing;
	}

	public void setMissing(boolean missing) {
		this.missing = missing;
	}
}
