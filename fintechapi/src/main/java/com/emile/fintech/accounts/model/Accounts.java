package com.emile.fintech.accounts.model;

import java.sql.Timestamp;
import java.util.List;

import com.emile.fintech.transactions.model.Transactions;
import com.emile.fintech.user.model.User;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;

/*@Embeddable /* made embeddable so it's instance variables marked w/ the embedded annotation
			   may be mapped to columns of the database table which the Transaction entity
			   is mapped to. */
			  /* note: for whatever reason, a class can't be marked w/ an entity annotation
			     and an embeddable annotation */
@Entity // maps class to database table.
public class Accounts {
	
	@EmbeddedId
	@AttributeOverrides({
	    @AttributeOverride(name = "routingNo", column = @Column(name = "routing_number")),
	    @AttributeOverride(name = "accountNo", column = @Column(name = "account_number"))
	    })
	private AccountsId compositeKey;
	
//	@Embedded // id is embedded from the Customer entity.
	private int id;
	
	private float balance; // the account balance w/ holds considered.
	
	@Column(name="account_type")
	private String accType; // defines whether the account is savings or checking.
	
//	@Embedded // dateTime is embedded from the Transactions entity.
//	@AttributeOverride(name = "dateTime", column = @Column(name="date_time"))
	@Column(name="date_time")
	private Timestamp dateTime; // date and time of when the purchase was made. From the Transaction microservice.
	
	private Timestamp eod; // definition of what is end of day.
	
	@Column(name="atm_debit_number")
	private String atmDebitCardNo; /* if customer has a checking account, they receive debit cards for checking
								   account which also functions as atm cards. */
	private String pin;
	
	@Column(name="exp_date")
	private String expDate;
	
	@Column(name="security_code")
	private String securityCode;
	
	@Column(name="freeze")
	private Boolean frozen; // allows customer to render the card unable to make a purchase for as long as
							// freeze is enabled.
	
	private Boolean missing; // report a card(s) as lost or stolen to automatically freeze the card.

	@Transient // removes the expectation that the instance variable will be saved to the database.
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "Accounts") /* one bank account can have many particular, contextually unique transactions; 
//																						   * on the Transaction entity side, many particular transactions can each only 
//																						   * be associated w/ one account, i.e. ManyToOne association. */
																						  /* Declaring an Account data field variable and marking it as @ManyToOne 
																						   * on the Transaction entity side is optional, so b/c I choose not to do so, 
																						   * this association unidirectional as oppose to bidirectional association. */
																						  /* CascadeType.ALL ensures the w.e. is done to the bank account table will be
																							 reflected on this list. */
																						  // FetchType defines the tenacity by which transaction data will be retrieved. 
																						  // Eager means data will be retrieved all at once.
																						  /* mappedBy defines which table the list will be mapped through the class
																							 representing the table. */
	private List<Transactions> transactions; // purchases and payments.

	@Transient
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "Accounts")
	private List<Transactions> hold; /* the amount of funds directed toward payments, but yet to be processed until
										end of day. */

	@Transient
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "Accounts")
	private List<Transactions> autoPay; /* Stores the vendorId from the Transaction microservice of vendors who have
										   requested automatic payment as authorized by their customer. */

	public AccountsId getCompositeKey() {
		return compositeKey;
	}
	
	public void setCompositeKey(AccountsId compositeKey) {
		this.compositeKey = compositeKey;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public Timestamp getDateTime() {
		return dateTime;
	}

	public void setDateTime(Timestamp dateTime) {
		this.dateTime = dateTime;
	}

	public Timestamp getEod() {
		return eod;
	}

	public void setEod(Timestamp eod) {
		this.eod = eod;
	}

	public String getAtmDebitCardNo() {
		return atmDebitCardNo;
	}

	public void setAtmDebitCardNo(String atmDebitCardNo) {
		this.atmDebitCardNo = atmDebitCardNo;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getExpDate() {
		return expDate;
	}

	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}

	public String getSecurityCode() {
		return securityCode;
	}

	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}

	public Boolean isFrozen() {
		return frozen;
	}

	public void setFrozen(Boolean frozen) {
		this.frozen = frozen;
	}

	public Boolean isMissing() {
		return missing;
	}

	public void setMissing(Boolean missing) {
		this.missing = missing;
	}

	public List<Transactions> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transactions> transactions) {
		this.transactions = transactions;
	}

	public List<Transactions> getHold() {
		return hold;
	}

	public void setHold(List<Transactions> hold) {
		this.hold = hold;
	}

	public List<Transactions> getAutoPay() {
		return autoPay;
	}

	public void setAutoPay(List<Transactions> autoPay) {
		this.autoPay = autoPay;
	}
}
