package com.emile.fintech.user.model;

import java.util.List;
import java.util.Set;

import com.emile.fintech.accounts.model.Accounts;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/*@Embeddable /* made embeddable so it's instance variables marked w/ the embedded annotation
			   may be mapped to columns of the database table which the Transaction entity
			   is mapped to. */
			  /* note: for whatever reason, a class can't be marked w/ an entity annotation
			   * and an embeddable annotation */
@Entity // maps class to database table.
//@Table(name = "USER") /* maps class to database table in addition to the Entity annotation, when class
//					     * name is different from table's name. */
public class User {

	@Id // maps instance variable to database table's primary key.
	@GeneratedValue(strategy = GenerationType.IDENTITY) /* this annotation circumvents the expectation that queries
													     * must insert values to the primary key. That is important b/c
													     * the column that this instance variable is mapped to, auto
													     * increments. */
	private Long id;
	
	private String username;
	
	@Column(name = "pass")
	private String password;
	
	@Column(name = "first_name")
	private String fName;
	
	@Column(name = "middle_name")
	private String mName;
	
	@Column(name = "last_name")
	private String lName;
	
	private String gender;
	
	@Column(name = "DOB")
	private String dob;
	
	@Column(name = "email_address")
	private String emailAddress;
	
	private String country;
	
	private String street;
	
	private String city;
	
	private String state;
	
	private String zipcode;
	
	private String ssn;

	@Transient // removes the expectation that the instance variable will be saved to the database.
//	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "Customer") /* many customers can have many particular bank accounts, i.e., joint and/or 
//																						    * single accounts. */
	                                                                                       /* CascadeType.ALL ensures the w.e. is done to the bank account table will be
	  																						* reflected on this list. */
																						   // FetchType defines the tenacity by which transaction data will be retrieved.
																						   // Eager means data will be retrieved all at once.
																						   /* mappedBy defines which table the list will be mapped through the class
																							* representing the table. */
	private List<Accounts> accounts; /* the many accounts a customer may have. Accounts are identified by
										composite primary key. */
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;

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

	public String getfName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getlName() {
		return lName;
	}

	public void setlName(String lName) {
		this.lName = lName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public List<Accounts> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Accounts> accounts) {
		this.accounts = accounts;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
}
